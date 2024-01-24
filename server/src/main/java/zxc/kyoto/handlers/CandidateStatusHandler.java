package zxc.kyoto.handlers;

import zxc.kyoto.entity.User;

import java.sql.ResultSet;

import static zxc.kyoto.dao.DataBaseService.getCandidateStatus;

public class CandidateStatusHandler implements Handler {
    @Override
    public String handle(User user, Object[] args) {
        try {
            String firstName = (String) args[1];
            String lastName = (String) args[2];
            ResultSet resultSet = getCandidateStatus(firstName, lastName);
            String infoSet = "Cтатус участника " + resultSet.getString("first_name") +
                    " " + resultSet.getString("last_name") + ": " + resultSet.getString("description") +
                    resultSet.getString("title") == null ? " " : "\nСейчас проходит испытание: " + resultSet.getString("title");
            return infoSet;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
