package net.mitocode.hexagonalArchitectureJava21.adapter.out.persistence.inmemory;

import net.mitocode.hexagonalArchitectureJava21.adapter.out.persistence.DemoGymClasses;
import net.mitocode.hexagonalArchitectureJava21.application.port.out.persistence.GymClassRepository;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.ClassId;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.GymClass;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryGymClassRepository implements GymClassRepository {

    private final Map<ClassId, GymClass> gymClasses = new ConcurrentHashMap<>();

    public InMemoryGymClassRepository(){
        createDemoGymClasses();
    }

    private void createDemoGymClasses(){
        DemoGymClasses.DEMO_GYM_CLASSES.forEach(this::save);
    }

    @Override
    public void save(GymClass gymClass){
        gymClasses.put(gymClass.id(), gymClass);
    }

    @Override
    public Optional<GymClass> findById(ClassId classId) {
        return Optional.ofNullable(gymClasses.get(classId));
    }

    @Override
    public List<GymClass> findByTypeOrDescription(String query) {
        String queryLowerCase = query.toLowerCase(Locale.ROOT);

        return gymClasses.values().stream()
                .filter(gymClass -> matchedQuery(gymClass, queryLowerCase))
                .collect(Collectors.toList());
    }

    private boolean matchedQuery(GymClass gymClass, String query){
        return gymClass.type().toLowerCase(Locale.ROOT).contains(query)
                || gymClass.description().toLowerCase(Locale.ROOT).contains(query);
    }
}
