package io.examples.saga.trip.commands;

import io.eventuate.tram.commands.common.Command;

/**
 * @author Gary Cheng
 */
public class RejectTripCommand implements Command {
    private Long tripId;

    public RejectTripCommand() {
    }

    public RejectTripCommand(Long tripId) {
        this.tripId = tripId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }
}
