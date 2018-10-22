package io.examples.saga.trip.repository;

import io.examples.saga.trip.domain.Trip;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Gary Cheng
 */
@Repository
public interface TripRepository extends CrudRepository<Trip, Long> {
}
