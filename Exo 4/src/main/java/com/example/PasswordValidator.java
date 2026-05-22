package com.example;

public class PasswordValidator {

    private static final String SPECIAL_CHARACTERS = "!@#$%";

    public boolean isValid(String password) {
        if (password == null) return false;
        if (password.length() < 8) return false;
        if (!containsLowercase(password)) return false;
        if (!containsUppercase(password)) return false;
        if (!containsDigit(password)) return false;
        if (!containsSpecial(password)) return false;
        return true;
    }

    public String getErrorMessage(String password) {
        if (password == null) return "Password must not be null";
        if (password.length() < 8) return "Password must contain at least 8 characters";
        if (!containsLowercase(password)) return "Password must contain at least one lowercase letter";
        if (!containsUppercase(password)) return "Password must contain at least one uppercase letter";
        if (!containsDigit(password)) return "Password must contain at least one digit";
        if (!containsSpecial(password)) return "Password must contain at least one special character";
        return "Password is valid";
    }

    private boolean containsLowercase(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) return true;
        }
        return false;
    }

    private boolean containsUppercase(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) return true;
        }
        return false;
    }

    private boolean containsDigit(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) return true;
        }
        return false;
    }

    private boolean containsSpecial(String password) {
        for (char c : password.toCharArray()) {
            if (SPECIAL_CHARACTERS.indexOf(c) >= 0) return true;
        }
        return false;
    }
}
