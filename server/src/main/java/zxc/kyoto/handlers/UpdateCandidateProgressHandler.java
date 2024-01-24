package zxc.kyoto.handlers;

import zxc.kyoto.dao.UsersService;
import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

import static zxc.kyoto.dao.DataBaseService.kickCandidate;

public class UpdateCandidateProgressHandler implements Handler{
    @Override
    public String handle(User user, Object[] args) {
        try {
            String[] candidates = (String[]) args[1];
            String[] newStatuses = (String[]) args[2];
            if (candidates.length != newStatuses.length) throw new IllegalArgumentException("Arrays length is not equals");
            if (UsersService.isUserExist(user.getUsername()) && (user.getRole() == Roles.ADMIN  || user.getRole() == Roles.WRITER)) {
                if (kickCandidate(candidates, newStatuses)) return "Success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "Fail";
    }
}
