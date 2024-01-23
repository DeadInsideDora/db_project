package zxc.kyoto.util;

import zxc.kyoto.entity.User;

public final class UserContainer {
    private static UserContainer INSTANCE = new UserContainer();
    private User user;

    public static UserContainer getInstance() {
        return INSTANCE;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
