package net.mitocode.hexagonalArchitectureJava21.model.gymclass;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public record ClassId(String value) {
    private static final String ALPHABET = "23456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int LENGTH_NEW_CLASS_IDS = 8;

    public ClassId {
        Objects.requireNonNull(value, "ClassId cannot be null");
        if (value.isEmpty()) {
            throw new IllegalArgumentException("ClassId cannot be empty");
        }
    }

    public static ClassId randomClassId() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder(LENGTH_NEW_CLASS_IDS);
        for (int i = 0; i < LENGTH_NEW_CLASS_IDS; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return new ClassId(sb.toString());
    }
}
