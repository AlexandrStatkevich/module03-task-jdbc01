package by.alst.je.jdbc.dao;

import by.alst.je.jdbc.entity.Flight;
import by.alst.je.jdbc.entity.Ticket;
import by.alst.je.jdbc.exception.DaoException;
import by.alst.je.jdbc.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class TicketDao {
    private final static TicketDao INSTANCE = new TicketDao();
    private final FlightDao flightDao = FlightDao.getInstance();


    private final static String FIND_ALL_SQL = """
            SELECT t.id, t.passport_no, t.passenger_name, t.flight_id, t.seat_no, t.cost,
            f.flight_no, f.departure_date, f.departure_airport_code, f.arrival_date, f.arrival_airport_code, f.aircraft_id, f.status
            FROM flight_repo.ticket t
            JOIN flight_repo.flight f on f.id = t.flight_id
            """;

    private final static String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE t.id = ?
            """;

    private final static String UPDATE_SQL = """
            UPDATE flight_repo.ticket
            SET passport_no = ?,
                passenger_name = ?,
                flight_id = ?,
                seat_no = ?,
                cost = ?
            WHERE id = ?
            """;

    private final static String MOST_COMMON_NAMES_SQL = """
            SELECT firstName, count(firstName) as nameNumber
                        FROM (SELECT split_part(passenger_name, ' ', 1) as firstName
                              FROM (SELECT passenger_name
                                    FROM flight_repo.ticket
                                    ORDER BY passenger_name) as pnt) as spnt
                        GROUP BY firstName
                        ORDER BY nameNumber DESC, firstName
                        LIMIT 5;
            """;

    private final static String PASSENGERS_AND_TICKETS_NUMBER_SQL = """
            SELECT passenger_name, count(passenger_name) as total_ticket
                                FROM flight_repo.ticket
                                GROUP BY passenger_name
                                ORDER BY total_ticket DESC, passenger_name;
            """;

    public Map<String, Integer> get5MostCommonNames() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(MOST_COMMON_NAMES_SQL)) {
            Map<String, Integer> names = new LinkedHashMap<>();
            var result = statement.executeQuery();
            while (result.next()) {
                names.put(result.getString("firstname"), result.getInt("nameNumber"));
            }
            return names;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Map<String, Integer> getPassengersAndTicketsNumber() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(PASSENGERS_AND_TICKETS_NUMBER_SQL)) {
            Map<String, Integer> names = new LinkedHashMap<>();
            var result = statement.executeQuery();
            while (result.next()) {
                names.put(result.getString("passenger_name"), result.getInt("total_ticket"));
            }
            return names;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean update(Ticket ticket) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, ticket.getPassportNo());
            statement.setString(2, ticket.getPassengerName());
            statement.setLong(3, ticket.getFlightId());
            statement.setString(4, ticket.getSeatNo());
            statement.setBigDecimal(5, ticket.getCoast());
            statement.setLong(6, ticket.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean updateByFlightId(Ticket ticket, Flight flight) {
        boolean result = false;
        if (ticket != null & flight != null) {
            if (Objects.equals(ticket.getFlightId(), flight.getId())) {
                Flight initialFlightValue = flightDao.findById(flight.getId()).orElse(null);
                Ticket initialTicketValue = findById(ticket.getId()).orElse(null);
                if (initialFlightValue != null & initialTicketValue != null) {
                    try {
                        result = flightDao.update(flight) & update(ticket);
                    } finally {
                        Flight resultFlightValue = flightDao.findById(flight.getId()).orElse(null);
                        Ticket resultTicketValue = findById(ticket.getId()).orElse(null);
                        if (resultFlightValue == null || resultTicketValue == null ||
                            !resultFlightValue.equals(flight) && !resultTicketValue.equals(ticket)) {
                            flightDao.update(initialFlightValue);
                            update(initialTicketValue);
                            result = false;
                        }
                    }
                }
            }
        }
        return result;
    }

    public Optional<Ticket> findById(Long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            Ticket ticket = null;

            var result = statement.executeQuery();
            while (result.next()) {
                ticket = buildTicket(result);
            }
            return Optional.ofNullable(ticket);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private Ticket buildTicket(ResultSet result) throws SQLException {
        return new Ticket(result.getLong("id"),
                result.getString("passport_no"),
                result.getString("passenger_name"),
                result.getLong("flight_id"),
                result.getString("seat_no"),
                result.getBigDecimal("cost")
        );
    }

    public static TicketDao getInstance() {
        return INSTANCE;
    }

    private TicketDao() {
    }
}
