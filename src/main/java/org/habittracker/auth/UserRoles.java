package org.habittracker.auth;

import jakarta.ws.rs.core.SecurityContext;

import java.util.Optional;

public class UserRoles {
    public static final String ADMIN = "admin";
    public static final String USER = "user";
    public static final String QA = "qa";

    public static String[] getAllRoles() {
        return new String[]{ADMIN, USER, QA};
    }

    public static Optional<String> getRoleName(SecurityContext context) {
        for (String role : getAllRoles()) {
            if (context.isUserInRole(role)) {
                return Optional.of(role);
            }
        }
        return Optional.empty();
    }
}
