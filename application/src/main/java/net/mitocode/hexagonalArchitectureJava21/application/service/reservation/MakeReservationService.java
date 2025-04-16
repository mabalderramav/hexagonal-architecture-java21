package net.mitocode.hexagonalArchitectureJava21.application.service.reservation;

import lombok.RequiredArgsConstructor;
import net.mitocode.hexagonalArchitectureJava21.application.port.in.reservation.GymClassNotFoundException;
import net.mitocode.hexagonalArchitectureJava21.application.port.in.reservation.MakeReservationUseCase;
import net.mitocode.hexagonalArchitectureJava21.application.port.out.persistence.GymClassRepository;
import net.mitocode.hexagonalArchitectureJava21.application.port.out.persistence.ReservationRepository;
import net.mitocode.hexagonalArchitectureJava21.model.customer.CustomerId;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.ClassId;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.NotEnoughSpotsAvailableException;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.Reservation;

import java.util.Objects;

@RequiredArgsConstructor
public class MakeReservationService implements MakeReservationUseCase {
    private final ReservationRepository reservationRepository;
    private final GymClassRepository gymClassRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Reservation makeReservation(CustomerId customerId, ClassId classId, int quantity)
            throws GymClassNotFoundException, NotEnoughSpotsAvailableException {
        Objects.requireNonNull(customerId, "Customer ID cannot be null");
        Objects.requireNonNull(classId, "Gym Class ID cannot be null");

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        // Validate gym class existence.
        var gymClass = gymClassRepository.findById(classId).orElseThrow(() ->
                new GymClassNotFoundException(String.format("ClassId with ID: %s not found", classId)));

        // Check if there are enough spots available.
        if (gymClass.spotsAvailable() < quantity) {
            throw new NotEnoughSpotsAvailableException("Not enough spots available for gym class with ID: "
                    .concat(String.valueOf(classId)), gymClass.spotsAvailable());
        }

        // Create a new reservation.
        var reservation = new Reservation(gymClass, customerId, quantity);

        // Save the reservation and update the gym class.
        gymClassRepository.save(reservation.gymClass());
        reservationRepository.save(reservation);

        return reservation;
    }
}
