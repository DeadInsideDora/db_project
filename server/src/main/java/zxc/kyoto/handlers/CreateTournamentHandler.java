package zxc.kyoto.handlers;

import zxc.kyoto.dao.UsersService;
import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

import java.sql.SQLException;

import static zxc.kyoto.dao.DataBaseService.createTournament;

public class CreateTournamentHandler implements Handler{
    @Override
    public String handle(User user, Object[] args) {
        String[] trials = (String[]) args[1];
        String trialsGroupTitle = (String) args[2];
        if (UsersService.isUserExist(user.getUsername()) && user.getRole() == Roles.ADMIN) {
            try {
                if (createTournament(trials, trialsGroupTitle)) return "Success";
            } catch (SQLException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }
        return "Fail";
    }
}
