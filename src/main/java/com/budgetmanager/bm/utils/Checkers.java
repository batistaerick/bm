package com.budgetmanager.bm.utils;

import java.util.regex.Pattern;

public class Checkers {

    private Checkers() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isPasswordCorrect(String password) {
        return Pattern.compile(
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&.])[A-Za-z\\d@$!%*?&.]{8,20}$"
        )
            .matcher(password)
            .matches();
    }
}
