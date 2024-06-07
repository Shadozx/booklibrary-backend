package com.shadoww.BookLibraryApp.models.user;

public enum Role {
    USER,
    ADMIN,
    SUPER_ADMIN;

    Role() {}

    public boolean equals(Role role) {
        if (role == null) return false;

        return this.getRoleName().equals(role.getRoleName());
    }


    public String getRoleName() {
        return  "ROLE_" + name();
    }

    @Override
    public String toString() {
        return "Role{" +
                "name=" + this.name() +
                '}';
    }
}
