package net.mitocode.hexagonalArchitectureJava21.application.service.reservation;

import net.mitocode.hexagonalArchitectureJava21.application.port.in.reservation.GymClassNotFoundException;
import net.mitocode.hexagonalArchitectureJava21.application.port.out.persistence.GymClassRepository;
import net.mitocode.hexagonalArchitectureJava21.application.port.out.persistence.ReservationRepository;
import net.mitocode.hexagonalArchitectureJava21.model.customer.CustomerId;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.ClassId;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.GymClass;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.TestGymClassFactory;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.NotEnoughSpotsAvailableException;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.ReservationStatus;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static net.mitocode.hexagonalArchitectureJava21.model.customer.TestCustomerIdFactory.randomCustomerId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MakeReservationServiceTest {
    private static final CustomerId TEST_CUSTOMER_ID = randomCustomerId();
    private static final GymClass TEST_GYM_CLASS_1 = TestGymClassFactory.createTestClass(30, 20);
    private static final GymClass TEST_GYM_CLASS_2 = TestGymClassFactory.createTestClass(40, 10);

    private final ReservationRepository reservationRepository = mock(ReservationRepository.class);
    private final GymClassRepository gymClassRepository = mock(GymClassRepository.class);
    private final MakeReservationService makeReservationService =
            new MakeReservationService(reservationRepository, gymClassRepository);

    @BeforeEach
    void setUp() {
        // Mocking the gym class repository to return a specific gym class when queried by ID.
        when(gymClassRepository.findById(TEST_GYM_CLASS_1.id())).thenReturn(Optional.of(TEST_GYM_CLASS_1));
        when(gymClassRepository.findById(TEST_GYM_CLASS_2.id())).thenReturn(Optional.of(TEST_GYM_CLASS_2));
    }

    @Test
    @DisplayName("given existing reservation")
    void givenExistingReservation() throws NotEnoughSpotsAvailableException, GymClassNotFoundException {
        var reservation = makeReservationService.makeReservation(TEST_CUSTOMER_ID, TEST_GYM_CLASS_1.id(), 2);

        verify(gymClassRepository).save(reservation.gymClass());
        verify(reservationRepository).save(reservation);

        assertThat(reservation.gymClass()).isEqualTo(TEST_GYM_CLASS_1);
        assertThat(reservation.spotsReserved()).isEqualTo(2);
        assertThat(reservation.status()).isEqualTo(ReservationStatus.CONFIRMED);
    }

    @Test
    @DisplayName("given unknown gym class id")
    void givenUnknownGymClassId() {
        var unknownGymClassId = ClassId.randomClassId();

        assertThatThrownBy(() -> makeReservationService.makeReservation(TEST_CUSTOMER_ID, unknownGymClassId, 2))
                .isInstanceOf(GymClassNotFoundException.class)
                .hasMessage(String.format("ClassId with ID: %s not found", unknownGymClassId));
    }

    @Test
    @DisplayName("given invalid quantity")
    void givenInvalidQuantity() {
        var invalidQuantity = 0; // Invalid quantity, must be greater than 0

        ThrowableAssert.ThrowingCallable callable = () ->
                makeReservationService.makeReservation(TEST_CUSTOMER_ID, TEST_GYM_CLASS_1.id(), invalidQuantity);

        assertThatIllegalArgumentException().isThrownBy(callable)
                .withMessage("Quantity must be greater than zero");
    }
}
