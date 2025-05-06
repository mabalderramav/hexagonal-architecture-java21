package net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.common;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;

/**
 * This class contains common methods for handling errors in REST controllers.
 * It provides methods to create error responses and exceptions.
 */
public class ControllerCommons {

    /**
     * Default constructor for ControllerCommons.
     * This constructor is intentionally empty.
     */
    public ControllerCommons() {
        // Constructor
    }

    /**
     * Creates a ClientErrorException with the specified status and error message.
     *
     * @param status  The HTTP status code.
     * @param message The error message.
     * @return A new ClientErrorException with the specified status and error message.
     */
    public static ClientErrorException clientErrorException(Response.Status status, String message) {
        return new ClientErrorException(errorResponse(status, message));
    }

    /**
     * Creates a Response object with the specified status and error message.
     *
     * @param status  The HTTP status code.
     * @param message The error message.
     * @return A new Response object with the specified status and error message.
     */
    public static Response errorResponse(Response.Status status, String message) {
        ErrorEntity errorEntity = new ErrorEntity(status.getStatusCode(), message);
        return Response.status(status)
                .entity(errorEntity)
                .build();
    }
}
