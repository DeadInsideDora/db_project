package zxc.kyoto.handlers;

import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

import static zxc.kyoto.dao.DataBaseService.getHuntersList;

public class HuntersInfoHandler implements Handler{
    @Override
    public String handle(User user, Object[] args) {
        try {
            if (user.getRole() != Roles.ADMIN)
                throw new IllegalArgumentException("Underprivileged");
            return getHuntersList();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
