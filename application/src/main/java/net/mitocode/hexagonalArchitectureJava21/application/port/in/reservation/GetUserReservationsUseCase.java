package net.mitocode.hexagonalArchitectureJava21.application.port.in.reservation;

import net.mitocode.hexagonalArchitectureJava21.model.customer.CustomerId;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.Reservation;

import java.util.List;

public interface GetUserReservationsUseCase {

    /**
     * Fetches all reservations for a given customer.
     *
     * @param customerId the ID of the customer whose reservations are to be fetched.
     * @return a list of reservations associated with the specified customer.
     */
    List<Reservation> getReservations(CustomerId customerId);
}
