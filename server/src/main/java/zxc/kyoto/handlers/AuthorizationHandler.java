package zxc.kyoto.handlers;

import zxc.kyoto.dao.UsersService;
import zxc.kyoto.entity.User;

public class AuthorizationHandler implements Handler{
    @Override
    public boolean handle(User user, Object[] args) {
        String username = (String) args[1];
        String password = (String) args[2];
        if (UsersService.isUserExist(username)) {
            return UsersService.authorization(username, password);
        } else {
            return UsersService.register(username,password);
        }
    }
}
