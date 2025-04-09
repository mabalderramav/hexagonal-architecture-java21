package net.mitocode.exagonalArchitectureJava21.model.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.mitocode.exagonalArchitectureJava21.model.customer.CustomerId;
import net.mitocode.exagonalArchitectureJava21.model.gymclass.GymClass;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class Reservation {
    private final GymClass gymClass;
    private final CustomerId customerId;
    private int spotsReserved;

    @Setter
    private ReservationStatus status = ReservationStatus.PENDING;

    public Reservation(GymClass gymClass, CustomerId customerId, int spotsReserved)
            throws NotEnoughSpotsAvailableException {
        if (spotsReserved <= 0) {
            throw new IllegalArgumentException("Number of spots reserved must be greater than zero");
        }
        this.gymClass = gymClass;
        this.customerId = customerId;
        this.spotsReserved = spotsReserved;
        confirm();
    }

    public void confirm() throws NotEnoughSpotsAvailableException {
        this.status = ReservationStatus.CONFIRMED;
        gymClass.reserveSpots(spotsReserved);
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
        gymClass.releaseSpots(spotsReserved);
    }
}
