package zxc.kyoto.handlers;

import zxc.kyoto.dao.DataBaseService;
import zxc.kyoto.entity.User;

import java.sql.ResultSet;

import static zxc.kyoto.dao.DataBaseService.getCandidateStatus;
import static zxc.kyoto.dao.DataBaseService.kickCandidate;

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
