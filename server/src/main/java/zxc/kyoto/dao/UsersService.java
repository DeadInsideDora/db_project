package zxc.kyoto.dao;

import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

import java.util.Hashtable;

public final class UsersService {
    public static Hashtable<String, User> users =  new Hashtable<>();

    public static boolean isUserExist(String username) {
        return users.containsKey(username);
    }

    public static boolean authorization(String username, String password, Roles role) {
        if (isUserExist(username)) {
            return users.get(username).getPassword().equals(password) && users.get(username).getRole().equals(role);
        }
        return false;
    }

    public static boolean register(String username, String password, Roles role) {
        if (isUserExist(username)) return false;
        users.put(username, new User(username, password, role));
        return true;
    }
}
