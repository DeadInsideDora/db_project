package zxc.kyoto.handlers;

import zxc.kyoto.dao.UsersService;
import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

import java.sql.SQLException;

import static zxc.kyoto.dao.DataBaseService.startTrial;

public class TrialBeginHandler implements Handler{
    @Override
    public String handle(User user, Object[] args) {
        try {
            String trialTitle = (String) args[1];
            if (UsersService.isUserExist(user.getUsername()) && (user.getRole() == Roles.ADMIN  || user.getRole() == Roles.WRITER)) {
                if (startTrial(trialTitle)) return "Success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "Fail";
    }
}
