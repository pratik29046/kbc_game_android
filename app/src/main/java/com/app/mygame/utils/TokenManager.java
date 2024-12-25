package com.app.mygame.utils;

public class TokenManager {

    private static String authToken = null;

    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String token) {
        authToken = token;
    }

    public static boolean isTokenValid() {
        return authToken != null && !authToken.isEmpty();
    }
}
