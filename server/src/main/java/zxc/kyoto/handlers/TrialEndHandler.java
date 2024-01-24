package zxc.kyoto.handlers;

import zxc.kyoto.dao.UsersService;
import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

import static zxc.kyoto.dao.DataBaseService.end_trial;

public class TrialEndHandler implements Handler{
    @Override
    public String handle(User user, Object[] args) {
        try {
            if (UsersService.isUserExist(user.getUsername()) && (user.getRole() == Roles.ADMIN  || user.getRole() == Roles.WRITER)) {
                if (end_trial()) return "Success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "Fail";
    }
}
