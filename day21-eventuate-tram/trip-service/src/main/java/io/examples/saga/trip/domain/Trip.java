package io.examples.saga.trip.domain;

import io.eventuate.tram.events.ResultWithEvents;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collections;

/**
 * @author Gary Cheng
 */

@Entity
@Table(name = "trips")
@Access(AccessType.FIELD)
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BookingState bookingState;
    @Embedded
    private TripDetail tripDetail;

    public Trip() {
    }

    public Trip(TripDetail tripDetail) {
        this.tripDetail = tripDetail;
        this.bookingState = BookingState.PENDING;
    }

    public static ResultWithEvents<Trip> createTrip(TripDetail orderDetails) {
        return new ResultWithEvents<>(new Trip(orderDetails), Collections.emptyList());
    }

    public void flightBooked() {
        this.bookingState = BookingState.APPROVED;
    }

    public void noteBookFlightFailed() {
        this.bookingState = BookingState.REJECTED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookingState getBookingState() {
        return bookingState;
    }

    public void setBookingState(BookingState bookingState) {
        this.bookingState = bookingState;
    }

    public TripDetail getTripDetail() {
        return tripDetail;
    }

    public void setTripDetail(TripDetail tripDetail) {
        this.tripDetail = tripDetail;
    }
}
