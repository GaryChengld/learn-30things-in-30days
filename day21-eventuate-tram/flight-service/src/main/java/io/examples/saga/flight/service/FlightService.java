package io.examples.saga.flight.service;

import io.examples.saga.flight.domain.Flight;
import io.examples.saga.flight.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Gary Cheng
 */
@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    public Flight createFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public Flight findFlightByFromAndTo(String from, String to) {
        List<Flight> flights = flightRepository.findByFromAndTo(from, to);
        if (flights.size() > 0) {
            return flights.get(0);
        } else {
            return null;
        }
    }
}
