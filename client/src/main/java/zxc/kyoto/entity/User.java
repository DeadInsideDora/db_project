package zxc.kyoto.entity;


import zxc.kyoto.util.Roles;

import java.io.Serializable;

public class User implements Serializable {
    private final String username;
    private final String password;
    private final Roles role;

    public User(String username, String password, Roles role) {
        this.username = username;
        this.password = password;
        this.role=role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Roles getRole() {
        return role;
    }

    @Override
    public String toString() {
        return username + " : " + password;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof User) {
            User userObj = (User) obj;
            return username.equals(userObj.getUsername()) && password.equals(userObj.getPassword());
        }
        return false;
    }
}
