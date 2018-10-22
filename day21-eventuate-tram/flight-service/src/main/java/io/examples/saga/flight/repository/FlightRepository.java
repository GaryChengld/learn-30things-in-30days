package io.examples.saga.flight.repository;

import io.examples.saga.flight.domain.Flight;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Gary Cheng
 */
public interface FlightRepository extends CrudRepository<Flight, Long> {
    List<Flight> findByFromAndTo(String from, String to);
}
