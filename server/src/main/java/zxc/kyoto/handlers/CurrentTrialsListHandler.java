package zxc.kyoto.handlers;

import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

import static zxc.kyoto.dao.DataBaseService.getTrialsInTournamentList;

public class CurrentTrialsListHandler implements Handler{
    @Override
    public String handle(User user, Object[] args) {
        try {
            if (user.getRole() != Roles.ADMIN && user.getRole() != Roles.WRITER)
                throw new IllegalArgumentException("Underprivileged");
            return getTrialsInTournamentList();
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
