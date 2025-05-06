package net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.common;

import jakarta.ws.rs.core.Response;
import net.mitocode.hexagonalArchitectureJava21.model.gymclass.ClassId;

import static net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.common.ControllerCommons.clientErrorException;

public class ClassIdParser {
    private ClassIdParser() {
        // Prevent instantiation
    }

    public static ClassId parseClassId(String string) {
        if (string == null || string.isEmpty()) {
            throw clientErrorException(Response.Status.BAD_REQUEST, "classId cannot be null or empty");
        }

        try {
            return new ClassId(string);
        } catch (IllegalArgumentException e) {
            throw clientErrorException(Response.Status.BAD_REQUEST, "Invalid ClassId format");
        }
    }
}
