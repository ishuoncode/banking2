package com.banking.eureka.util;

import java.util.Random;

public class AccountNumberGenerator {

    private static final Random RANDOM = new Random();

    public static String generateAccountNumber() {
        long number = 100000000000L +
                (long)(RANDOM.nextDouble() * 900000000000L);
        return String.valueOf(number);
    }
}
