package zxc.kyoto.handlers;

import zxc.kyoto.dao.UsersService;
import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

import java.sql.SQLException;

import static zxc.kyoto.dao.DataBaseService.addHunter;

public class HunterAddHandler implements Handler{
    @Override
    public String handle(User user, Object[] args) {
        String firstName = (String) args[1];
        String lastName = (String) args[2];
        String post = (String) args[3];
        if (UsersService.isUserExist(user.getUsername()) && user.getRole() == Roles.ADMIN) {
            try {
                if (addHunter(firstName, lastName, post)) return "Success";
            } catch (SQLException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }
        return "Fail";
    }
}
