package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidatorTest {

    private final PasswordValidator validator = new PasswordValidator();


    // ----- Tests classiques -----

    @Test
    void shouldReturnTrueWhenPasswordIsValid() {
        // Arrange
        String password = "Password1!";

        // Act
        boolean result = validator.isValid(password);

        // Assert
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenPasswordIsNull() {
        // Act
        boolean result = validator.isValid(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void shouldReturnValidMessageWhenPasswordIsValid() {
        // Arrange
        String password = "Admin2024@";

        // Act
        String message = validator.getErrorMessage(password);

        // Assert
        assertEquals("Password is valid", message);
    }


    // ----- Tests paramétrés avec @CsvSource -----

    @ParameterizedTest
    @CsvSource({
            "Password1!, true",
            "Admin2024@, true",
            "short1!,    false",
            "PASSWORD1!, false",
            "password1!, false",
            "Password!,  false",
            "Password1,  false"
    })
    void shouldValidatePasswords(String password, boolean expected) {
        assertEquals(expected, validator.isValid(password));
    }

    @ParameterizedTest
    @CsvSource({
            "'',           Password must contain at least 8 characters",
            "short1!,      Password must contain at least 8 characters",
            "PASSWORD1!,   Password must contain at least one lowercase letter",
            "password1!,   Password must contain at least one uppercase letter",
            "Password!,    Password must contain at least one digit",
            "Password1,    Password must contain at least one special character",
            "Password1!,   Password is valid"
    })
    void shouldReturnExpectedErrorMessage(String password, String expectedMessage) {
        assertEquals(expectedMessage, validator.getErrorMessage(password));
    }


    // ----- Test avec @ValueSource -----

    @ParameterizedTest
    @ValueSource(strings = {"Password1!", "Admin2024@", "Hello1#word", "Test1234$", "Qwerty1%"})
    void shouldAcceptValidPasswords(String password) {
        assertTrue(validator.isValid(password));
    }


    // ----- Test avec @MethodSource -----

    @ParameterizedTest
    @MethodSource("invalidPasswordsProvider")
    void shouldRejectInvalidPasswords(String password, String expectedMessage) {
        assertFalse(validator.isValid(password));
        assertEquals(expectedMessage, validator.getErrorMessage(password));
    }

    static Stream<Arguments> invalidPasswordsProvider() {
        return Stream.of(
                Arguments.of(null, "Password must not be null"),
                Arguments.of("short1!", "Password must contain at least 8 characters"),
                Arguments.of("PASSWORD1!", "Password must contain at least one lowercase letter"),
                Arguments.of("password1!", "Password must contain at least one uppercase letter"),
                Arguments.of("Password!", "Password must contain at least one digit"),
                Arguments.of("Password1", "Password must contain at least one special character")
        );
    }


    // ----- Bonus : test avec @NullAndEmptySource -----

    @ParameterizedTest
    @NullAndEmptySource
    void shouldRejectNullAndEmptyPasswords(String password) {
        assertFalse(validator.isValid(password));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldReturnExpectedErrorForNullAndEmpty(String password) {
        String expected = (password == null)
                ? "Password must not be null"
                : "Password must contain at least 8 characters";
        assertEquals(expected, validator.getErrorMessage(password));
    }
}
