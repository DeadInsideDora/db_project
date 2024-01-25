package zxc.kyoto.handlers;

import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

import static zxc.kyoto.dao.DataBaseService.endTournament;
import static zxc.kyoto.dao.DataBaseService.getNewHunters;

public class EndTournamentHandler implements Handler{
    @Override
    public String handle(User user, Object[] args) {
        try {
            if (user.getRole() != Roles.ADMIN)
                throw new IllegalArgumentException("Underprivileged");
            if (endTournament()) {
                return getNewHunters();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "Fail";
    }
}
