package net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.reservation;

import io.restassured.response.Response;
import jakarta.ws.rs.core.Application;
import net.mitocode.hexagonalArchitectureJava21.application.port.in.reservation.CancelReservationUseCase;
import net.mitocode.hexagonalArchitectureJava21.application.port.in.reservation.GetUserReservationsUseCase;
import net.mitocode.hexagonalArchitectureJava21.application.port.in.reservation.GymClassNotFoundException;
import net.mitocode.hexagonalArchitectureJava21.application.port.in.reservation.MakeReservationUseCase;
import net.mitocode.hexagonalArchitectureJava21.model.customer.CustomerId;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.ClassId;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.GymClass;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.TestGymClassFactory;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.NotEnoughSpotsAvailableException;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.Reservation;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.ReservationNotFoundException;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.CONFLICT;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.Status.NO_CONTENT;
import static net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.HttpTestCommons.TEST_PORT;
import static net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.HttpTestCommons.assertThatResponseIsError;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReservationControllerTest {

    private static final CustomerId TEST_CUSTOMER_ID = new CustomerId("test@example.com");
    private static final GymClass TEST_GYM_CLASS_1 = TestGymClassFactory.createTestClass(20, 15);

    private static final MakeReservationUseCase MAKE_RESERVATION_USE_CASE = mock(MakeReservationUseCase.class);
    private static final GetUserReservationsUseCase GET_USER_RESERVATIONS_USE_CASE = mock(GetUserReservationsUseCase.class);
    private static final CancelReservationUseCase CANCEL_RESERVATION_USE_CASE = mock(CancelReservationUseCase.class);

    private static UndertowJaxrsServer server;

    @BeforeAll
    static void init() {
        server = new UndertowJaxrsServer()
                .setPort(TEST_PORT)
                .start()
                .deploy(new Application() {
                    @Override
                    public Set<Object> getSingletons() {
                        return Set.of(
                                new MakeReservationController(MAKE_RESERVATION_USE_CASE),
                                new GetReservationsController(GET_USER_RESERVATIONS_USE_CASE),
                                new CancelReservationController(CANCEL_RESERVATION_USE_CASE)
                        );
                    }
                });
    }

    @AfterAll
    static void stop() {
        server.stop();
    }

    @BeforeEach
    void resetMocks() {
        Mockito.reset(MAKE_RESERVATION_USE_CASE, GET_USER_RESERVATIONS_USE_CASE, CANCEL_RESERVATION_USE_CASE);
    }

    @Test
    void givenInvalidCustomerId_getReservations_returnsError() {
        String customerId = "invalid-email";

        Response response = given().port(TEST_PORT)
                .get("/reservations/" + customerId)
                .then()
                .extract()
                .response();

        assertThatResponseIsError(response, BAD_REQUEST, "Invalid email format");
    }

    @Test
    void givenValidCustomerId_getReservations_returnsReservations() throws NotEnoughSpotsAvailableException {
        CustomerId customerId = TEST_CUSTOMER_ID;

        GymClass gymClass1 = TestGymClassFactory.createTestClass(10, 5);
        GymClass gymClass2 = TestGymClassFactory.createTestClass(15, 7);

        Reservation reservation1 = new Reservation(gymClass1, customerId, 3);
        Reservation reservation2 = new Reservation(gymClass2, customerId, 2);

        when(GET_USER_RESERVATIONS_USE_CASE.getReservations(customerId))
                .thenReturn(List.of(reservation1, reservation2));

        Response response = given()
                .port(TEST_PORT)
                .get("/reservations/" + customerId.email())
                .then()
                .extract()
                .response();

        ReservationControllerAssertions.assertThatResponseContainsReservations(response, List.of(reservation1, reservation2));
    }

    @Test
    void givenValidData_makeReservation_addsReservation() throws NotEnoughSpotsAvailableException, GymClassNotFoundException {
        CustomerId customerId = TEST_CUSTOMER_ID;
        GymClass gymClass = TEST_GYM_CLASS_1;
        ClassId classId = gymClass.id();
        int quantity = 3;

        Reservation expectedReservation = new Reservation(gymClass, customerId, quantity);

        when(MAKE_RESERVATION_USE_CASE.makeReservation(customerId, classId, quantity))
                .thenReturn(expectedReservation);

        Response response = given()
                .port(TEST_PORT)
                .queryParam("classId", classId.value())
                .queryParam("quantity", quantity)
                .post("/reservations/" + customerId.email() + "/reservation")
                .then()
                .extract()
                .response();

        ReservationControllerAssertions.assertThatResponseIsReservation(response, expectedReservation);
    }

    @Test
    void givenClassNotFound_makeReservation_returnsError() throws GymClassNotFoundException, NotEnoughSpotsAvailableException {
        CustomerId customerId = TEST_CUSTOMER_ID;
        ClassId classId = ClassId.randomClassId();
        int quantity = 5;

        when(MAKE_RESERVATION_USE_CASE.makeReservation(customerId, classId, quantity))
                .thenThrow(new GymClassNotFoundException());

        Response response = given().port(TEST_PORT)
                .queryParam("classId", classId.value())
                .queryParam("quantity", quantity)
                .post("/reservations/" + customerId.email() + "/reservation")
                .then()
                .extract()
                .response();

        assertThatResponseIsError(response, NOT_FOUND, "The requested class does not exist");
    }

    @Test
    void givenNotEnoughSpots_makeReservation_returnsError() throws NotEnoughSpotsAvailableException, GymClassNotFoundException {
        CustomerId customerId = TEST_CUSTOMER_ID;
        ClassId classId = TEST_GYM_CLASS_1.id();
        int quantity = 20;

        when(MAKE_RESERVATION_USE_CASE.makeReservation(customerId, classId, quantity))
                .thenThrow(new NotEnoughSpotsAvailableException("Not enough spots available", 10));

        Response response = given().port(TEST_PORT)
                .queryParam("classId", classId.value())
                .queryParam("quantity", quantity)
                .post("/reservations/" + customerId.email() + "/reservation")
                .then()
                .extract()
                .response();

        assertThatResponseIsError(response, CONFLICT, "Only 10 spots available. Not enough spots available.");
    }

    @Test
    void givenValidCustomerId_cancelReservation_invokesCancelUseCase() throws ReservationNotFoundException {
        CustomerId customerId = TEST_CUSTOMER_ID;
        ClassId classId = TEST_GYM_CLASS_1.id();

        given().port(TEST_PORT)
                .delete("/reservations/" + customerId.email() + "/class/" + classId.value())
                .then()
                .statusCode(NO_CONTENT.getStatusCode());

        verify(CANCEL_RESERVATION_USE_CASE).cancelReservation(customerId, classId);
    }
}