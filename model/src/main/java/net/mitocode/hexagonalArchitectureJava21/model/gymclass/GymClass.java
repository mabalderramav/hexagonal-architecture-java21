package net.mitocode.hexagonalArchitectureJava21.model.gymclass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import net.mitocode.hexagonalArchitectureJava21.model.reservation.NotEnoughSpotsAvailableException;

@Data
@Accessors(fluent = true)
@AllArgsConstructor
public class GymClass {
    private final ClassId id;
    private String type;
    private String description;
    private int capacity;
    private int spotsAvailable;

    public void reserveSpots(int spots) throws NotEnoughSpotsAvailableException {
        if (spots > spotsAvailable) {
            throw new NotEnoughSpotsAvailableException("Not enough spots available", spotsAvailable);
        }
        spotsAvailable -= spots;
    }

    public void releaseSpots(int spots) {
        spotsAvailable += spots;
    }
}
