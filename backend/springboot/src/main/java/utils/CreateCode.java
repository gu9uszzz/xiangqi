package utils;

import java.util.concurrent.ThreadLocalRandom;

public class CreateCode {
    public static String generateVerificationCode() {
        int code = ThreadLocalRandom.current().nextInt(1000000);
        return String.format("%06d", code);
    }
}
