package net.mitocode.exagonalArchitectureJava21.model.customer;

import java.util.regex.Pattern;

public record CustomerId(String email) {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public CustomerId {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (email.length() < 5) {
            throw new IllegalArgumentException("invalid format");
        }
    }
}
