package net.mitocode.hexagonalArchitectureJava21.application.port.out.persistence;

import net.mitocode.hexagonalArchitectureJava21.model.gymclass.ClassId;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.GymClass;

import java.util.List;
import java.util.Optional;

public interface GymClassRepository {
    /**
     * Finds gym classes by type or description.
     *
     * @param query the search query for type or description
     * @return a list of gym classes matching the query
     */
    List<GymClass> findByTypeOrDescription(String query);

    /**
     * Saves a gym class to the repository.
     *
     * @param gymClass the gym class to save.
     */
    void save(GymClass gymClass);

    /**
     * Finds a gym class by its ID.
     * @param id the ID of the gym class to find.
     * @return an Optional containing the found gym class, or empty if not found.
     */
    Optional<GymClass> findById(ClassId id);
}
