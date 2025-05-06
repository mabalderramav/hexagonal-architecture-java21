package net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.gymclass;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import net.mitocode.hexagonalArchitectureJava21.application.port.in.gymclass.FindGymClassUseCase;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.GymClass;

import java.util.List;

import static net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.common.ControllerCommons.clientErrorException;

@Path("/gym-classes")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class FindGymClassController {
    private final FindGymClassUseCase findGymClassUseCase;

    @GET
    public List<GymClassInListWebModel> findGymClasses(@QueryParam("query") String query) {
        if (query == null || query.isBlank()) {
            throw  clientErrorException(Response.Status.BAD_REQUEST, "Query parameter is required");
        }

        List<GymClass> gymClasses;
        try {
            gymClasses = findGymClassUseCase.findByTypeOrDescription(query);
        } catch (IllegalArgumentException e) {
            throw clientErrorException(Response.Status.BAD_REQUEST, "Invalid 'query'");
        }

        if (gymClasses.isEmpty()) {
            throw clientErrorException(Response.Status.NOT_FOUND, "No gym classes found");
        }

        return gymClasses.stream()
                .map(GymClassInListWebModel::fromDomainModel)
                .toList();
    }
}
