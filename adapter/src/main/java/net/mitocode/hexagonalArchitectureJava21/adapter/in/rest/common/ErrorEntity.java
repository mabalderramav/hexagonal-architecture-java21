package net.mitocode.hexagonalArchitectureJava21.adapter.in.rest.common;

public record ErrorEntity(int httpStatus, String errorMessage) {
}
