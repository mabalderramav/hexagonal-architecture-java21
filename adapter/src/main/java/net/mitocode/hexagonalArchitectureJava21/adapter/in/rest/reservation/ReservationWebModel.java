package net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.reservation;

import net.mitocode.hexagonalArchitectureJava21.model.customer.CustomerId;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.ClassId;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.Reservation;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.ReservationStatus;

public record ReservationWebModel(
        ClassId classId,
        String classType,
        String classDescription,
        CustomerId customerId,
        int spotsReserved,
        ReservationStatus status
) {
    public static ReservationWebModel fromDomainModel(Reservation reservation) {
        return new ReservationWebModel(
                reservation.gymClass().id(),
                reservation.gymClass().type(),
                reservation.gymClass().description(),
                reservation.customerId(),
                reservation.spotsReserved(),
                reservation.status()
        );
    }
}
