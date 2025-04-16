package net.mitocode.hexagonalArchitectureJava21.application.service.reservation;

import net.mitocode.hexagonalArchitectureJava21.application.port.out.persistence.ReservationRepository;
import net.mitocode.hexagonalArchitectureJava21.model.customer.CustomerId;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.GymClass;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.TestGymClassFactory;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.NotEnoughSpotsAvailableException;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.Reservation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static net.mitocode.hexagonalArchitectureJava21.model.customer.TestCustomerIdFactory.randomCustomerId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetUserReservationsServiceTest {
    private static final CustomerId TEST_CUSTOMER_ID = randomCustomerId();
    private static final GymClass TEST_GYM_CLASS_1 = TestGymClassFactory.createTestClass();

    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final GetUserReservationsService getUserReservationsService =
            new GetUserReservationsService(reservationRepository);

    @Test
    @DisplayName("get reservation persisted")
    void getReservationPersisted() throws NotEnoughSpotsAvailableException {
        var persistedReservation = new Reservation(TEST_GYM_CLASS_1, TEST_CUSTOMER_ID, 2);

        // Mock the repository to return the persisted reservation when queried by customer ID.
        when(reservationRepository.findReservationsByCustomerId(TEST_CUSTOMER_ID))
                .thenReturn(List.of(persistedReservation));

        var persistedResult = getUserReservationsService.getReservations(TEST_CUSTOMER_ID);
        // Verify that the returned reservations match the persisted ones.
        assertThat(persistedResult).contains(persistedReservation);
    }

    @Test
    @DisplayName("reservation is not persisted")
    void reservationIsNotPersisted() {
        // Mock the repository to return an empty list when queried by customer ID.
        when(reservationRepository.findReservationsByCustomerId(TEST_CUSTOMER_ID))
                .thenReturn(Collections.emptyList());

        var reservations = getUserReservationsService.getReservations(TEST_CUSTOMER_ID);
        // Verify that the returned reservations list is empty.
        assertThat(reservations).isNotNull();
        assertThat(reservations).isEmpty();
    }
}
