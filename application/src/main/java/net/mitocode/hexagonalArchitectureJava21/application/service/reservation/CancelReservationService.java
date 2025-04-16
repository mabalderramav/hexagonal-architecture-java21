package net.mitocode.hexagonalArchitectureJava21.application.service.reservation;

import lombok.RequiredArgsConstructor;
import net.mitocode.hexagonalArchitectureJava21.application.port.in.reservation.CancelReservationUseCase;
import net.mitocode.hexagonalArchitectureJava21.application.port.out.persistence.GymClassRepository;
import net.mitocode.hexagonalArchitectureJava21.application.port.out.persistence.ReservationRepository;
import net.mitocode.hexagonalArchitectureJava21.model.customer.CustomerId;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.ClassId;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.ReservationNotFoundException;

import java.util.Objects;

@RequiredArgsConstructor
public class CancelReservationService implements CancelReservationUseCase {
    private final ReservationRepository reservationRepository;
    private final GymClassRepository gymClassRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelReservation(CustomerId customerId, ClassId classId) throws ReservationNotFoundException {
        Objects.requireNonNull(customerId, "Customer ID cannot be null");
        Objects.requireNonNull(classId, "Class ID cannot be null");

        // Check if the reservation exists.
        var reservation = reservationRepository.findByCustomerIdAndClassId(customerId, classId)
                .orElseThrow(() -> new ReservationNotFoundException(
                        String.format("No reservation found for customer ID: %s and class ID: %s", customerId, classId)));

        // Cancel the reservation and update the gym class.
        reservation.cancel();
        // Update the gym class to reflect the cancellation.
        gymClassRepository.save(reservation.gymClass());
        // Delete the reservation from the repository.
        reservationRepository.deleteReservationByCustomerIdAndClassId(customerId, classId);
    }
}
