package meli.challenge.test.utils;

import java.util.UUID;

public class UUIDHelper {

    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
