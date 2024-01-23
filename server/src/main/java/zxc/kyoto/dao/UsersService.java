package zxc.kyoto.dao;

import java.util.Hashtable;

public class UsersService {
    private static Hashtable<String, String> usersAndPasswords = new Hashtable<>();

    public UsersService() {
        UsersService.usersAndPasswords.put("admin", "admin");
    }

    public static boolean isUserExist(String username) {
        return usersAndPasswords.containsKey(username);
    }

    public static boolean authorization(String username, String password) {
        if (isUserExist(username)) {
            return usersAndPasswords.get(username) == password;
        }
        return false;
    }

    public static boolean register(String username, String password) {
        if (isUserExist(username)) return false;
        usersAndPasswords.put(username, password);
        return true;
    }
}
