package by.alst.je.jdbc.main;

import by.alst.je.jdbc.dao.FlightDao;
import by.alst.je.jdbc.dao.TicketDao;
import by.alst.je.jdbc.entity.Flight;
import by.alst.je.jdbc.entity.FlightStatus;
import by.alst.je.jdbc.entity.Ticket;

import java.math.BigDecimal;

public class JdbcRunner {
    public static void main(String[] args) {

        var ticketDao = TicketDao.getInstance();
        var flightDao = FlightDao.getInstance();

        System.out.println("5 Most CommonNames:");
        System.out.println(ticketDao.get5MostCommonNames());
        System.out.println();

        System.out.println("Passengers And Tickets Number:");
        ticketDao.getPassengersAndTicketsNumber().entrySet().forEach(System.out::println);
        System.out.println();

        System.out.println("Ticket Update By Id");
        Ticket ticket;
        ticket = ticketDao.findById(5L).get();
        System.out.println(ticket);
        ticket.setPassportNo("passport");
        ticket.setPassengerName("Genadee");
        ticket.setSeatNo("15C");
        ticket.setCoast(BigDecimal.valueOf(12.15));
        boolean result = ticketDao.update(ticket);
        System.out.println("result = " + result + ": setPassportNo(\"passport\") setPassengerName(\"Genadee\") " +
                           "+ setSeatNo(\"15C\") setCoast(BigDecimal.valueOf(12.15))");
        System.out.println(ticketDao.findById(ticket.getId()).orElse(null));
        System.out.println();

        System.out.println("Ticket And Flight Update By FlightId:");
        System.out.println("Ticket Flight_Id = Flight Id");
        ticket = ticketDao.findById(8L).get();
        System.out.println(ticket);
        Flight flight = flightDao.findById(ticket.getFlightId()).get();
        System.out.println(flight);
        flight.setStatus(FlightStatus.CANCELLED);
        ticket.setCoast(BigDecimal.valueOf(12.45));
        result = ticketDao.updateByFlightId(ticket, flight);
        ticket = ticketDao.findById(ticket.getId()).orElse(null);
        System.out.println("result = " + result + ": setStatus(FlightStatus.CANCELLED) " +
                           "setCoast(BigDecimal.valueOf(12.45)) ");
        System.out.println(ticket);
        System.out.println(flightDao.findById(ticket.getFlightId()).get());
        System.out.println();

        System.out.println("Ticket Flight_Id != Flight Id");
        flight = flightDao.findById(3L).orElse(null);
        System.out.println(ticket);
        System.out.println(flight);
        flight.setStatus(FlightStatus.SCHEDULED);
        ticket.setCoast(BigDecimal.valueOf(55.45));
        result = ticketDao.updateByFlightId(ticket, flight);
        System.out.println("result = " + result + ": setStatus(FlightStatus.SCHEDULED) " +
                           "setCoast(BigDecimal.valueOf(55.45))");
        System.out.println(ticketDao.findById(ticket.getId()).orElse(null));
        System.out.println(flightDao.findById(3L).orElse(null));
    }
}
