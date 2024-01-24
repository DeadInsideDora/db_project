package zxc.kyoto.handlers;

import zxc.kyoto.dao.UsersService;
import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

import java.sql.SQLException;

import static zxc.kyoto.dao.DataBaseService.addTrial;

public class TrialAddHandler implements Handler {
    @Override
    public String handle(User user, Object[] args) {
        try {
            String trialName = (String) args[1];
            String trialDescription = (String) args[2];
            String[] organizators = (String[]) args[3];
            String[] rules = (String[]) args[4];
            if (UsersService.isUserExist(user.getUsername()) && user.getRole() == Roles.ADMIN) {
                if (addTrial(trialName, trialDescription, organizators, rules)) return "Success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "Fail";
    }
}
