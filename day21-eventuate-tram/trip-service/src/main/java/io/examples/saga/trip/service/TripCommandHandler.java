package io.examples.saga.trip.service;

import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import io.examples.saga.trip.commands.ApproveTripCommand;
import io.examples.saga.trip.commands.RejectTripCommand;
import io.examples.saga.trip.domain.Trip;
import io.examples.saga.trip.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

/**
 * @author Gary Cheng
 */
public class TripCommandHandler {
    @Autowired
    private TripRepository tripRepository;

    public CommandHandlers commandHandlerDefinitions() {
        return SagaCommandHandlersBuilder
                .fromChannel("orderService")
                .onMessage(ApproveTripCommand.class, this::approve)
                .onMessage(RejectTripCommand.class, this::reject)
                .build();
    }

    public Message approve(CommandMessage<ApproveTripCommand> cm) {
        long tripId = cm.getCommand().getTripId();
        Trip trip = tripRepository.findById(tripId).get();
        trip.flightBooked();
        return withSuccess();
    }

    public Message reject(CommandMessage<RejectTripCommand> cm) {
        long tripId = cm.getCommand().getTripId();
        Trip trip = tripRepository.findById(tripId).get();
        trip.noteBookFlightFailed();
        return withSuccess();
    }
}
