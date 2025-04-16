package net.mitocode.hexagonalArchitectureJava21.application.port.in.reservation;

import net.mitocode.hexagonalArchitectureJava21.model.customer.CustomerId;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.ClassId;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.ReservationNotFoundException;

public interface CancelReservationUseCase {

    /**
     * Cancels a reservation for a specific customer and gym class.
     *
     * @param customerId the ID of the customer whose reservation is to be canceled.
     * @param classId    the ID of the gym class for which the reservation is to be canceled.
     * @throws ReservationNotFoundException if no reservation exists for the given customer and class ID.
     */
    void cancelReservation(CustomerId customerId, ClassId classId) throws ReservationNotFoundException;
}
