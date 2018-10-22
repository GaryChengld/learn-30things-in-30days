package io.examples.saga.trip.controller;

import io.examples.saga.trip.domain.Trip;
import io.examples.saga.trip.domain.TripDetail;
import io.examples.saga.trip.repository.TripRepository;
import io.examples.saga.trip.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author Gary Cheng
 */
@RestController
public class TripController {
    @Autowired
    private TripService tripService;
    @Autowired
    private TripRepository tripRepository;

    @RequestMapping(value = "/trips", method = RequestMethod.POST)
    public Trip createOrder(@RequestBody TripDetail tripDetail) {
        Trip trip = tripService.createTrip(tripDetail);
        return trip;
    }

    @RequestMapping(value = "/trips/{tripId}", method = RequestMethod.GET)
    public ResponseEntity<Trip> getOrder(@PathVariable Long tripId) {
        Optional<Trip> trip = tripRepository.findById(tripId);
        if (!trip.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(trip.get());
        }
    }
}
