package io.examples.saga.flight.controller;

import io.examples.saga.flight.domain.Flight;
import io.examples.saga.flight.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Gary Cheng
 */
@RestController
public class FlightController {
    @Autowired
    private FlightService flightService;

    @RequestMapping(value = "/flights", method = RequestMethod.POST)
    public Flight createCustomer(@RequestBody Flight flight) {
        return flightService.createFlight(flight);
    }
}
