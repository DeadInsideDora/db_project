package zxc.kyoto.handlers;

import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

import static zxc.kyoto.dao.DataBaseService.getCandidatesInProcessInfo;
import static zxc.kyoto.dao.DataBaseService.getTrialInProcessInfo;

public class TrialInProcessInfoHandler implements Handler {
    @Override
    public String handle(User user, Object[] args) {
        try {
            if (user.getRole() != Roles.ADMIN && user.getRole() != Roles.WRITER)
                throw new IllegalArgumentException("Underprivileged");
            String infoSet = getTrialInProcessInfo();
            infoSet+=getCandidatesInProcessInfo();
            return infoSet;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
