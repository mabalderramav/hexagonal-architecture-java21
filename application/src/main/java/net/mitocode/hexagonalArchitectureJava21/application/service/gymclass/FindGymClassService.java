package net.mitocode.hexagonalArchitectureJava21.application.service.gymclass;

import lombok.RequiredArgsConstructor;
import net.mitocode.hexagonalArchitectureJava21.application.port.in.gymclass.FindGymClassUSeCase;
import net.mitocode.hexagonalArchitectureJava21.application.port.out.persistence.GymClassRepository;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.GymClass;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class FindGymClassService implements FindGymClassUSeCase {

    private final GymClassRepository gymClassRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GymClass> findByTypeOrDescription(String query) {
        Objects.requireNonNull(query, "Query must not be null");
        if (query.isBlank()) {
            throw new IllegalArgumentException("Query must not be empty");
        }
        if (query.length() < 2) {
            throw new IllegalArgumentException("Query must be at least 2 characters long");
        }

        return gymClassRepository.findByTypeOrDescription(query);
    }
}
