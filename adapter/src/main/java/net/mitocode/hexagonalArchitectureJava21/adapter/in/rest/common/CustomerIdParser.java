package net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.common;

import jakarta.ws.rs.core.Response;
import net.mitocode.hexagonalArchitectureJava21.model.customer.CustomerId;

import static net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.common.ControllerCommons.clientErrorException;

public class CustomerIdParser {
    private CustomerIdParser() {
        // Prevent instantiation
    }

    public static CustomerId parseCustomerId(String string) {
        if (string == null || string.isEmpty()) {
            throw clientErrorException(Response.Status.BAD_REQUEST, "email cannot be null or empty");
        }

        try {
            return new CustomerId(string);
        } catch (IllegalArgumentException e) {
            throw clientErrorException(Response.Status.BAD_REQUEST, "Invalid email format");
        }
    }
}
