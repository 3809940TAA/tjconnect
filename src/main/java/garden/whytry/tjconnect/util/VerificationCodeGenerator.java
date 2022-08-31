package garden.whytry.tjconnect.util;

import java.security.SecureRandom;

public final class VerificationCodeGenerator {
    private static final SecureRandom RANDOM = new SecureRandom();

    private VerificationCodeGenerator() {
    }

    public static String new4DigitsCode() {
        return "%04d".formatted(RANDOM.nextInt(10000));
    }
}
