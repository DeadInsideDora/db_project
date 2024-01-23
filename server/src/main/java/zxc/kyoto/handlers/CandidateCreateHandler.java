package zxc.kyoto.handlers;

import zxc.kyoto.dao.DataBaseService;
import zxc.kyoto.dao.UsersService;
import zxc.kyoto.entity.User;

import java.sql.SQLException;

public class CandidateCreateHandler implements Handler{

    @Override
    public boolean handle(User user, Object[] args) {
        String firstName = (String) args[0];
        String lastName = (String) args[1];
        String[] facts = (String[]) args[2];
        String[] factsWeights = (String[]) args[3];
        if (UsersService.isUserExist(user.getUsername())) {
            try {
                DataBaseService.addСandidate(firstName, lastName, facts, factsWeights);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

}
