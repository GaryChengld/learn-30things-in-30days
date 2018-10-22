package io.examples.saga.trip.service;

import io.eventuate.tram.events.ResultWithEvents;
import io.eventuate.tram.sagas.orchestration.SagaManager;
import io.examples.saga.trip.domain.Trip;
import io.examples.saga.trip.domain.TripDetail;
import io.examples.saga.trip.repository.TripRepository;
import io.examples.saga.trip.sagas.CreateTripSagaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Gary Cheng
 */
@Service
public class TripService {
    @Autowired
    private SagaManager<CreateTripSagaData> createTripSagaManager;

    @Autowired
    private TripRepository tripRepository;

    @Transactional
    public Trip createTrip(TripDetail tripDetail) {
        ResultWithEvents<Trip> resultWithEvents = Trip.createTrip(tripDetail);
        Trip trip = resultWithEvents.result;
        tripRepository.save(trip);
        CreateTripSagaData data = new CreateTripSagaData(trip.getId(), tripDetail);
        createTripSagaManager.create(data, Trip.class, trip.getId());
        return trip;
    }
}
