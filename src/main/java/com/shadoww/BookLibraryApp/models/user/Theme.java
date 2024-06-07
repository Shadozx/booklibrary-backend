package com.shadoww.BookLibraryApp.models.user;

public enum Theme {
    LIGHT, DARK;

    public static Theme next(Theme theme) {
        return switch (theme) {
            case DARK -> LIGHT;
            case LIGHT -> DARK;
        };
    }
}
