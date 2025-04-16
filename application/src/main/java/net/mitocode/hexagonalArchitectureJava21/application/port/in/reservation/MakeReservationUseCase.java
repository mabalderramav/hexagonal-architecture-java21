package net.mitocode.hexagonalArchitectureJava21.application.port.in.reservation;

import net.mitocode.hexagonalArchitectureJava21.model.customer.CustomerId;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.ClassId;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.NotEnoughSpotsAvailableException;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.Reservation;

public interface MakeReservationUseCase {
    Reservation makeReservation(CustomerId customerId, ClassId gymClassId, int quantity)
        throws GymClassNotFoundException, NotEnoughSpotsAvailableException;
}
