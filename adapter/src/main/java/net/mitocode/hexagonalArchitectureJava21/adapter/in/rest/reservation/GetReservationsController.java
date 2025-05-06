package net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.reservation;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import net.mitocode.hexagonalArchitectureJava21.application.port.in.reservation.GetUserReservationsUseCase;
import net.mitocode.hexagonalArchitectureJava21.model.customer.CustomerId;

import java.util.List;

import static net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.common.CustomerIdParser.parseCustomerId;

@Path("/reservations")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class GetReservationsController {
    private final GetUserReservationsUseCase getUserReservationsUseCase;

    @GET
    @Path("/{customerId}")
    public List<ReservationWebModel> getUserReservations(@PathParam("customerId") String customerIdString) {
        CustomerId customerId = parseCustomerId(customerIdString);
        return getUserReservationsUseCase.getReservations(customerId)
                .stream()
                .map(ReservationWebModel::fromDomainModel)
                .toList();
    }
}
