package by.alst.je.jdbc.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Ticket {
    private Long id;
    private String passportNo;
    private String passengerName;
    private Long flightId;
    private String seatNo;
    private BigDecimal coast;

    public Ticket() {
    }

    public Ticket(Long id, String passportNo, String passengerName, Long flightId, String seatNo, BigDecimal coast) {
        this.id = id;
        this.passportNo = passportNo;
        this.passengerName = passengerName;
        this.flightId = flightId;
        this.seatNo = seatNo;
        this.coast = coast;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassportNo() {
        return passportNo;
    }

    public void setPassportNo(String passportNo) {
        this.passportNo = passportNo;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public BigDecimal getCoast() {
        return coast;
    }

    public void setCoast(BigDecimal coast) {
        this.coast = coast;
    }

    @Override
    public String toString() {
        return "Ticket{" +
               "id=" + id +
               ", passportNo='" + passportNo + '\'' +
               ", passengerName='" + passengerName + '\'' +
               ", flightId=" + flightId +
               ", seatNo='" + seatNo + '\'' +
               ", coast=" + coast +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) && Objects.equals(passportNo, ticket.passportNo)
               && Objects.equals(passengerName, ticket.passengerName) && Objects.equals(flightId, ticket.flightId)
               && Objects.equals(seatNo, ticket.seatNo) && Objects.equals(coast, ticket.coast);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, passportNo, passengerName, flightId, seatNo, coast);
    }
}
