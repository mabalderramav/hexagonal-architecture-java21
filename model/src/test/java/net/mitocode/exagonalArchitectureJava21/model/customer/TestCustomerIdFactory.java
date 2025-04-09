package net.mitocode.exagonalArchitectureJava21.model.customer;

import java.util.UUID;

public class TestCustomerIdFactory {
    public static CustomerId randomCustomerId() {
        String randomEmail = "user" + UUID.randomUUID() + "@example.com";
        return new CustomerId(randomEmail);
    }
}
