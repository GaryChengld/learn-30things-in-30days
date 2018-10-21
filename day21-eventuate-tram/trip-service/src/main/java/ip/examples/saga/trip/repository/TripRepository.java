package ip.examples.saga.trip.repository;

import ip.examples.saga.trip.domain.Trip;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Gary Cheng
 */
public interface TripRepository extends CrudRepository<Trip, Long> {
}
