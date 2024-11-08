package org.constants;

public class Constants {
    public static final String BASE_URL = "https://api.github.com";
    public static final String COMMAND = "^github-activity\\s+([a-zA-Z0-9_]{3,15})$";

    public static class Endpoints {
        public static final String USER_EVENTS = "/users/%s/events";
        // Other endpoint constants
    }

    public static class Headers {
        public static final String ACCEPT_JSON = "application/json";
        // Other header constants
    }
}
