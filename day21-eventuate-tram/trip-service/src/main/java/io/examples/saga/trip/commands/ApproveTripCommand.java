package io.examples.saga.trip.commands;

import io.eventuate.tram.commands.common.Command;

/**
 * @author Gary Cheng
 */
public class ApproveTripCommand implements Command {
    private Long tripId;

    public ApproveTripCommand() {
    }

    public ApproveTripCommand(Long tripId) {
        this.tripId = tripId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }
}
