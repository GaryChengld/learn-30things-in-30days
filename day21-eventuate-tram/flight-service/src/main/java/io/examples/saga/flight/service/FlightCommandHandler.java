package io.examples.saga.flight.service;

import io.eventuate.tram.commands.consumer.CommandHandlers;
import io.eventuate.tram.commands.consumer.CommandMessage;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.participant.SagaCommandHandlersBuilder;
import io.examples.saga.flight.api.commands.BookFlightCommand;
import io.examples.saga.flight.api.replies.FlightBookFailed;
import io.examples.saga.flight.api.replies.FlightBooked;
import io.examples.saga.flight.domain.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withFailure;
import static io.eventuate.tram.commands.consumer.CommandHandlerReplyBuilder.withSuccess;

/**
 * @author Gary Cheng
 */
@Component
public class FlightCommandHandler {
    @Autowired
    private FlightService flightService;

    public CommandHandlers commandHandlerDefinitions() {
        return SagaCommandHandlersBuilder
                .fromChannel("flightService")
                .onMessage(BookFlightCommand.class, this::reserveCredit)
                .build();
    }

    public Message reserveCredit(CommandMessage<BookFlightCommand> cm) {
        BookFlightCommand cmd = cm.getCommand();
        Flight flight = flightService.findFlightByFromAndTo(cmd.getFromCity(), cmd.getToCity());
        if (flight != null) {
            return withSuccess(new FlightBooked());
        } else {
            return withFailure(new FlightBookFailed());
        }
    }
}
