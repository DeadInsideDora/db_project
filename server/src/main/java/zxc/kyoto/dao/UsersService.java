package zxc.kyoto.dao;

import java.util.Hashtable;

public final class UsersService {
    public static Hashtable<String, String> usersAndPasswords =  new Hashtable<>();

    public static boolean isUserExist(String username) {
        System.out.println(usersAndPasswords.size());
        return usersAndPasswords.containsKey(username);
    }

    public static boolean authorization(String username, String password) {
        if (isUserExist(username)) {
            return usersAndPasswords.get(username).equals(password);
        }
        return false;
    }

    public static boolean register(String username, String password) {
        if (isUserExist(username)) return false;
        usersAndPasswords.put(username, password);
        return true;
    }
}
