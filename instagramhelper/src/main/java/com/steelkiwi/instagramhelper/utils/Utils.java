package com.steelkiwi.instagramhelper.utils;

public final class Utils {
    public static <T> T checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }
}