package io.examples.saga.trip.sagas;

import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.sagas.orchestration.SagaDefinition;
import io.eventuate.tram.sagas.simpledsl.SimpleSaga;
import io.examples.saga.flight.api.commands.BookFlightCommand;
import io.examples.saga.trip.commands.ApproveTripCommand;
import io.examples.saga.trip.commands.RejectTripCommand;

import static io.eventuate.tram.commands.consumer.CommandWithDestinationBuilder.send;

/**
 * @author Gary Cheng
 */
public class CreateTripSaga implements SimpleSaga<CreateTripSagaData> {
    private SagaDefinition<CreateTripSagaData> sagaDefinition =
            step()
                    .withCompensation(this::reject)
                    .step()
                    .invokeParticipant(this::bookFlight)
                    .step()
                    .invokeParticipant(this::approve)
                    .build();


    @Override
    public SagaDefinition<CreateTripSagaData> getSagaDefinition() {
        return this.sagaDefinition;
    }


    private CommandWithDestination bookFlight(CreateTripSagaData data) {
        long tripId = data.getTripId();
        return send(new BookFlightCommand(tripId, data.getTripDetail().getTripDate(), data.getTripDetail().getFromCity(), data.getTripDetail().getToCity()))
                .to("flightService")
                .build();
    }

    public CommandWithDestination reject(CreateTripSagaData data) {
        return send(new RejectTripCommand(data.getTripId()))
                .to("orderService")
                .build();
    }

    private CommandWithDestination approve(CreateTripSagaData data) {
        return send(new ApproveTripCommand(data.getTripId()))
                .to("orderService")
                .build();
    }
}
