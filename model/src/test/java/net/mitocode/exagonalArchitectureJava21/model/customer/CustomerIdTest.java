package net.mitocode.exagonalArchitectureJava21.model.customer;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class CustomerIdTest {
    @ParameterizedTest
    @ValueSource(strings =
            {
                    "invalid-email",
                    "invalid-email@",
                    "@invalid-email.com",
                    "invalid-email@.com",
                    "invalid-email@com",
                    "invalid-email@com.",
                    "test1"
            })
    void testInvalidEmail(String email) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new CustomerId(email))
                .withMessage("Invalid email format");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  ", "   "})
    void testBlankEmail(String email) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new CustomerId(email))
                .withMessage("Email cannot be null or blank");
    }

    @ParameterizedTest
    @ValueSource(strings = {"miguel.aldo1698@gmail.com"})
    void testValidEmail(String email) {
        var customerId = new CustomerId(email);
        assertThat(customerId.email()).isEqualTo(email);
    }
}
