package zxc.kyoto.handlers;

import zxc.kyoto.dao.UsersService;
import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

public class AuthorizationHandler implements Handler {
    @Override
    public String handle(User user, Object[] args) {
        try {
            String username = (String) args[1];
            String password = (String) args[2];
            return UsersService.authorization(username, password) ? UsersService.getRoleByUser(username).getRole() : "Fail";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
