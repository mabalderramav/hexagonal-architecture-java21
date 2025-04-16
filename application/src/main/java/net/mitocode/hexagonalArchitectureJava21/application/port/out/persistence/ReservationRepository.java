package net.mitocode.hexagonalArchitectureJava21.application.port.out.persistence;

import net.mitocode.hexagonalArchitectureJava21.model.customer.CustomerId;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.ClassId;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    /**
     * Saves a reservation to the repository.
     *
     * @param reservation the reservation to save.
     */
    void save(Reservation reservation);

    /**
     * Finds all reservations associated with a specific customer ID.
     *
     * @param customerId the ID of the customer whose reservations are to be found.
     * @return a list of reservations for the specified customer.
     */
    List<Reservation> findReservationsByCustomerId(CustomerId customerId);

    /**
     * Finds a reservation by customer ID and gym class ID.
     *
     * @param customerId the ID of the customer.
     * @param gymClassId the ID of the gym class.
     * @return an Optional containing the reservation if found, or empty if not found.
     */
    Optional<Reservation> findByCustomerIdAndClassId(CustomerId customerId, ClassId gymClassId);

    /**
     * Deletes a reservation by customer ID and gym class ID.
     *
     * @param customerId the ID of the customer.
     * @param gymClassId the ID of the gym class.
     */
    void deleteReservationByCustomerIdAndClassId(CustomerId customerId, ClassId gymClassId);

    /**
     * Deletes all reservations associated with a specific customer ID.
     *
     * @param customerId the ID of the customer whose reservations are to be deleted.
     */
    void deleteAllByCustomerId(CustomerId customerId);
}
