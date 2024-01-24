package zxc.kyoto.handlers;

import zxc.kyoto.dao.DataBaseService;
import zxc.kyoto.dao.UsersService;
import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

import java.sql.SQLException;

import static zxc.kyoto.util.Roles.*;

public class CandidateCreateHandler implements Handler{

    @Override
    public String handle(User user, Object[] args) {
        String firstName = (String) args[0];
        String lastName = (String) args[1];
        String[] facts = (String[]) args[2];
        String[] factsWeights = (String[]) args[3];
        if (UsersService.isUserExist(user.getUsername()) && (user.getRole()== ADMIN || user.getRole() == WRITER)) {
            try {
                DataBaseService.addСandidate(firstName, lastName, facts, factsWeights);
            } catch (SQLException e) {
                e.printStackTrace();
                return e.getMessage();
            }
            return "Success";
        }
        return "Fail";
    }

}
