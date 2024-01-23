package zxc.kyoto.util;

public enum Roles {
    ADMIN("admin"),
    WRITER("writer"),
    VIEWER("viewer");

    private final String role;

    Roles(String role) {
        this.role=role;
    }

    public String getRole() {
        return role;
    }

    public static Roles getRoleByStr(String str) {
        for (Roles role : values()) {
            if (role.getRole().equals(str)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Role was not found.");
    }
}
