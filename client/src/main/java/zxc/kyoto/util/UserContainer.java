package zxc.kyoto.util;

import zxc.kyoto.entity.User;

public class UserContainer {
    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserContainer.user = user;
    }
}
