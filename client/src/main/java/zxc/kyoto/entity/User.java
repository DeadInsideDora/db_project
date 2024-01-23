package zxc.kyoto.entity;


import java.io.Serializable;

public class User implements Serializable {
    private final String username;
    private final String password;
    private final boolean signIn;

    public User(String username, String password, boolean sign) {
        this.username = username;
        this.password = password;
        this.signIn=sign;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isSignIn() {
        return signIn;
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
