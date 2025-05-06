package net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.reservation;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.mitocode.hexagonalArchitectureJava21.application.port.in.reservation.GymClassNotFoundException;
import net.mitocode.hexagonalArchitectureJava21.application.port.in.reservation.MakeReservationUseCase;
import net.mitocode.hexagonalArchitectureJava21.model.customer.CustomerId;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.ClassId;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.NotEnoughSpotsAvailableException;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.Reservation;

import static net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.common.ClassIdParser.parseClassId;
import static net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.common.ControllerCommons.clientErrorException;
import static net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.common.CustomerIdParser.parseCustomerId;

@Path("/reservations")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class MakeReservationController {
    private final MakeReservationUseCase makeReservationUseCase;

    @POST
    @Path("/{customerId}/reservation")
    public ReservationWebModel makeReservation(
            @PathParam("customerId") String customerIdString,
            @QueryParam("classId") String classIdString,
            @QueryParam("quantity") int quantityInt
    ) {
        CustomerId customerId = parseCustomerId(customerIdString);
        ClassId classId = parseClassId(classIdString);

        try {
            Reservation reservation = makeReservationUseCase.makeReservation(customerId, classId, quantityInt);
            return ReservationWebModel.fromDomainModel(reservation);
        } catch (IllegalArgumentException e) {
            throw clientErrorException(Response.Status.BAD_REQUEST, "Invalid reservation: ".concat(e.getMessage()));
        } catch (GymClassNotFoundException e) {
            throw clientErrorException(Response.Status.NOT_FOUND, "The requested class does not exist");
        } catch (NotEnoughSpotsAvailableException e) {
            throw clientErrorException(Response.Status.CONFLICT,
                    "Only %d spots available. Not enough spots available.".formatted(e.availableSpots()));
        }
    }
}
