package io.examples.saga.trip.sagas;

import io.examples.saga.trip.domain.TripDetail;

/**
 * @author Gary Cheng
 */
public class CreateTripSagaData {
    private Long tripId;
    private TripDetail tripDetail;

    public CreateTripSagaData() {
    }

    public CreateTripSagaData(Long tripId, TripDetail tripDetail) {
        this.tripId = tripId;
        this.tripDetail = tripDetail;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public TripDetail getTripDetail() {
        return tripDetail;
    }

    public void setTripDetail(TripDetail tripDetail) {
        this.tripDetail = tripDetail;
    }
}
