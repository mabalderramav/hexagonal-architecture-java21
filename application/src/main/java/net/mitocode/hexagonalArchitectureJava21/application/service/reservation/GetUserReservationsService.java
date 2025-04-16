package net.mitocode.hexagonalArchitectureJava21.application.service.reservation;

import lombok.RequiredArgsConstructor;
import net.mitocode.hexagonalArchitectureJava21.application.port.in.reservation.GetUserReservationsUseCase;
import net.mitocode.hexagonalArchitectureJava21.application.port.out.persistence.ReservationRepository;
import net.mitocode.hexagonalArchitectureJava21.model.customer.CustomerId;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.Reservation;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class GetUserReservationsService implements GetUserReservationsUseCase {
    private final ReservationRepository reservationRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Reservation> getReservations(CustomerId customerId) {
        Objects.requireNonNull(customerId, "Customer ID cannot be null");

        // Retrieve all reservations for the specified customer ID.
        return reservationRepository.findReservationsByCustomerId(customerId);
    }
}
