package io.examples.saga.flight.api.commands;

import io.eventuate.tram.commands.common.Command;

/**
 * @author Gary Cheng
 */
public class BookFlightCommand implements Command {
    private Long tripId;
    private String flightDate;
    private String fromCity;
    private String toCity;

    public BookFlightCommand() {
    }

    public BookFlightCommand(Long tripId, String flightDate, String fromCity, String toCity) {
        this.tripId = tripId;
        this.flightDate = flightDate;
        this.fromCity = fromCity;
        this.toCity = toCity;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }
}
