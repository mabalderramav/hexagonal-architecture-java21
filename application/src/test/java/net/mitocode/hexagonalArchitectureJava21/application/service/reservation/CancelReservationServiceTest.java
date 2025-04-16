package net.mitocode.hexagonalArchitectureJava21.application.service.reservation;

import net.mitocode.hexagonalArchitectureJava21.application.port.out.persistence.GymClassRepository;
import net.mitocode.hexagonalArchitectureJava21.application.port.out.persistence.ReservationRepository;
import net.mitocode.hexagonalArchitectureJava21.model.customer.CustomerId;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.GymClass;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.TestGymClassFactory;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.NotEnoughSpotsAvailableException;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.Reservation;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.ReservationNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static net.mitocode.hexagonalArchitectureJava21.model.customer.TestCustomerIdFactory.randomCustomerId;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CancelReservationServiceTest {
    private static final CustomerId TEST_CUSTOMER_ID = randomCustomerId();
    private static final GymClass TEST_GYM_CLASS_1 = TestGymClassFactory.createTestClass(30, 20);

    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final GetUserReservationsService getUserReservationsService =
            new GetUserReservationsService(reservationRepository);
    private final GymClassRepository gymClassRepository = mock(GymClassRepository.class);
    private final CancelReservationService cancelReservationService =
            new CancelReservationService(reservationRepository, gymClassRepository);

    @Test
    @DisplayName("empty reservation invoke delete")
    void emptyReservationInvokeDelete() throws NotEnoughSpotsAvailableException, ReservationNotFoundException {
        var testReservation = new Reservation(TEST_GYM_CLASS_1, TEST_CUSTOMER_ID, 1);
        // Mock the repository to return an empty list when queried by customer ID.
        when(reservationRepository.findByCustomerIdAndClassId( TEST_CUSTOMER_ID, TEST_GYM_CLASS_1.id()))
                .thenReturn(Optional.of(testReservation));

        doNothing().when(reservationRepository).deleteReservationByCustomerIdAndClassId(
                TEST_CUSTOMER_ID, TEST_GYM_CLASS_1.id()
        );

        cancelReservationService.cancelReservation(TEST_CUSTOMER_ID, TEST_GYM_CLASS_1.id());
        verify(reservationRepository).deleteReservationByCustomerIdAndClassId(TEST_CUSTOMER_ID, TEST_GYM_CLASS_1.id());
    }
}
