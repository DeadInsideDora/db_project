package zxc.kyoto.handlers;

import zxc.kyoto.dao.UsersService;
import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import static zxc.kyoto.dao.DataBaseService.saveInteraction;

public class InteractionHandler implements Handler{
    @Override
    public String handle(User user, Object[] args) {
        try {
            String[] actors = (String[]) args[1];
            String groupDescription = (String) args[2];
            String interaction = (String) args[3];
            Timestamp startTimestamp = new Timestamp(((Date) args[4]).getTime());
            Timestamp endTimestamp = new Timestamp(((Date) args[5]).getTime());
            if (UsersService.isUserExist(user.getUsername()) && (user.getRole() == Roles.ADMIN  || user.getRole() == Roles.WRITER)) {
                if (saveInteraction(actors, groupDescription, interaction, startTimestamp, endTimestamp)) return "Success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "Fail";
    }
}
