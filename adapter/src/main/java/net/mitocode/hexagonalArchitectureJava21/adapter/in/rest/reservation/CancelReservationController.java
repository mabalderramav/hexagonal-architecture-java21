package net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.reservation;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.mitocode.hexagonalArchitectureJava21.application.port.in.reservation.CancelReservationUseCase;
import net.mitocode.hexagonalArchitectureJava21.model.customer.CustomerId;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.ClassId;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.ReservationNotFoundException;

import static net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.common.ClassIdParser.parseClassId;
import static net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.common.ControllerCommons.clientErrorException;
import static net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.common.CustomerIdParser.parseCustomerId;

@Path("/reservations")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class CancelReservationController {
    private final CancelReservationUseCase cancelReservationUseCase;

    @DELETE
    @Path("/{customerId}/class/{classId}")
    public void cancelReservation(@PathParam("customerId") String customerIdString, @PathParam("classId") String classIdString) {
        CustomerId customerId = parseCustomerId(customerIdString);
        ClassId classId = parseClassId(classIdString);

        try {
            cancelReservationUseCase.cancelReservation(customerId, classId);
        } catch (ReservationNotFoundException e) {
            throw clientErrorException(Response.Status.NOT_FOUND, "Reservation not found: ".concat(e.getMessage()));
        }
    }
}
