package zxc.kyoto.handlers;

import zxc.kyoto.dao.UsersService;
import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

public class RegisterHandler implements Handler {
    @Override
    public String handle(User user, Object[] args) {
        String username = (String) args[1];
        String password = (String) args[2];
        try {
            Roles role = Roles.getRoleByStr((String) args[3]);
            if (role == Roles.ADMIN || role == Roles.WRITER && (user == null || user.getRole() != Roles.ADMIN))
                throw new IllegalArgumentException("Underprivileged");
            return UsersService.register(username, password, role) ? "Register was succeed" : "Register was failed";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
