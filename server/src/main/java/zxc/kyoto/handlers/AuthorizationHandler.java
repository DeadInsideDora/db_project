package zxc.kyoto.handlers;

import zxc.kyoto.dao.UsersService;
import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

public class AuthorizationHandler implements Handler{
    @Override
    public String handle(User user, Object[] args) {
        String username = (String) args[1];
        String password = (String) args[2];
        try {
            Roles role = Roles.getRoleByStr((String) args[3]);
            return UsersService.authorization(username, password, role) ? "Authorization success" : "Authorization fail";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
