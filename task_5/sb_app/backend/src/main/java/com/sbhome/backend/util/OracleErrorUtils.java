package com.sbhome.backend.util;

public final class OracleErrorUtils {

    private OracleErrorUtils() {
    }

    public static String extractCustomMessage(String rawMessage, String oraCode) {
        int start = rawMessage.indexOf(oraCode);
        if (start == -1) {
            return rawMessage;
        }
        start += oraCode.length();
        int end = rawMessage.indexOf('\n', start);
        if (end == -1) {
            end = rawMessage.length();
        }
        return rawMessage.substring(start, end).trim();
    }
}