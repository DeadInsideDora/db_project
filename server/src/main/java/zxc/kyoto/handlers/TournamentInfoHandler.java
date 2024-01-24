package zxc.kyoto.handlers;

import zxc.kyoto.dao.UsersService;
import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

import java.sql.ResultSet;

import static zxc.kyoto.dao.DataBaseService.getTournamentInfo;

public class TournamentInfoHandler implements Handler {
    @Override
    public String handle(User user, Object[] args) {
        try {
            if (user.getRole() != Roles.ADMIN)
                throw new IllegalArgumentException("Underprivileged");
            return getTournamentInfo();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
