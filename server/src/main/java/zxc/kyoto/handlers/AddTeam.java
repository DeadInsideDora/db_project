package zxc.kyoto.handlers;

import zxc.kyoto.dao.UsersService;
import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

import java.sql.SQLException;

import static zxc.kyoto.dao.DataBaseService.addMembersToTeam;

public class AddTeam implements Handler {
    @Override
    public String handle(User user, Object[] args) {
        try {
            String teamTitle = (String) args[1];
            String teamDescription = (String) args[2];
            String[] members = (String[]) args[3];
            if (UsersService.isUserExist(user.getUsername()) && (user.getRole() == Roles.ADMIN || user.getRole() == Roles.WRITER)) {
                if (addMembersToTeam(teamTitle, teamDescription, members)) return "Success";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "Fail";
    }
}
