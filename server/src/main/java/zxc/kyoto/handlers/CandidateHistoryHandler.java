package zxc.kyoto.handlers;

import zxc.kyoto.entity.User;

import java.sql.ResultSet;

import static zxc.kyoto.dao.DataBaseService.getHistByCandidate;

public class CandidateHistoryHandler implements Handler{
    @Override
    public String handle(User user, Object[] args) {
        try {
            String firstName = (String) args[1];
            String lastName = (String) args[2];
            ResultSet resultSet = getHistByCandidate(firstName, lastName);
            String infoSet = "Сводка по участнику " + firstName + " " + lastName + ": \n";
            while(resultSet.next()) {
                infoSet+= "Завершил испытание " + resultSet.getString("title") + " со статусом " + resultSet.getString("description") + "\n";
            }
            return infoSet;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
