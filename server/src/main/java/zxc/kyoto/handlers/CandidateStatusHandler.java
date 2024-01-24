package zxc.kyoto.handlers;

import zxc.kyoto.entity.User;

import static zxc.kyoto.dao.DataBaseService.getCandidateStatus;

public class CandidateStatusHandler implements Handler {
    @Override
    public String handle(User user, Object[] args) {
        try {
            String firstName = (String) args[1];
            String lastName = (String) args[2];
            return getCandidateStatus(firstName, lastName);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
