package net.mitocode.hexagonalArchitectureJava21.application.port.in.gymclass;

import net.mitocode.hexagonalArchitectureJava21.model.gymclass.GymClass;

import java.util.List;

public interface FindGymClassUseCase {
    /**
     * Finds gym classes by type or description.
     *
     * @param query the search query for type or description
     * @return a list of gym classes matching the query
     */
    List<GymClass> findByTypeOrDescription(String query);
}
