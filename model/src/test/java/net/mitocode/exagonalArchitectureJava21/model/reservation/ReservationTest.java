package net.mitocode.exagonalArchitectureJava21.model.reservation;

import net.mitocode.exagonalArchitectureJava21.model.gymclass.GymClass;
import net.mitocode.exagonalArchitectureJava21.model.gymclass.TestGymClassFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static net.mitocode.exagonalArchitectureJava21.model.customer.TestCustomerIdFactory.randomCustomerId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReservationTest {

    @Test
    @DisplayName("given a valid reservation")
    void testValidReservation() throws NotEnoughSpotsAvailableException {
        GymClass gymClass = TestGymClassFactory.createTestClass(10, 10);
        Reservation reservation = new Reservation(gymClass, randomCustomerId(), 5);
        assertThat(gymClass.spotsAvailable()).isEqualTo(5);
    }

    @Test
    @DisplayName("Given a reservation with insufficient spots")
    void testReservationWithInsufficientSpots() {
        GymClass gymClass = TestGymClassFactory.createTestClass(10, 5);
        assertThrows(NotEnoughSpotsAvailableException.class, () -> {
            new Reservation(gymClass, randomCustomerId(), 6);
        });
    }

    @Test
    @DisplayName("Given a reservation with confirmed status")
    void testReleaseSpots() throws NotEnoughSpotsAvailableException {
        GymClass gymClass = TestGymClassFactory.createTestClass(10, 10);
        Reservation reservation = new Reservation(gymClass, randomCustomerId(), 3);
        assertThat(reservation.status()).isEqualTo(ReservationStatus.CONFIRMED);
    }

    @Test
    @DisplayName("Given a reservation with cancelled status and check status")
    void testCancelReservation() throws NotEnoughSpotsAvailableException {
        GymClass gymClass = TestGymClassFactory.createTestClass(10, 10);
        Reservation reservation = new Reservation(gymClass, randomCustomerId(), 3);
        reservation.cancel();
        assertThat(reservation.status()).isEqualTo(ReservationStatus.CANCELLED);
        assertThat(gymClass.spotsAvailable()).isEqualTo(10);
    }

    @Test
    @DisplayName("Given a negative spots value")
    void testNegativeSpots() {
        GymClass gymClass = TestGymClassFactory.createTestClass(10, 10);
        assertThrows(IllegalArgumentException.class, () -> {
            new Reservation(gymClass, randomCustomerId(), -1);
        });
    }
}
