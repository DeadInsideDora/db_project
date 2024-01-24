package zxc.kyoto.handlers;

import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

import java.sql.ResultSet;

import static zxc.kyoto.dao.DataBaseService.getCandidatesInProcessInfo;
import static zxc.kyoto.dao.DataBaseService.getTrialInProcessInfo;

public class TrialInProcessInfoHandler implements Handler {
    @Override
    public String handle(User user, Object[] args) {
        try {
            if (user.getRole() != Roles.ADMIN && user.getRole() != Roles.WRITER)
                throw new IllegalArgumentException("Underprivileged");
            ResultSet resultSet = getTrialInProcessInfo();
            String infoSet = "Текущее испытание: " + resultSet.getString("title") + "\n" +
                    "\tОписание: " + resultSet.getString("description") + "\n" +
                    "Организатор: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name") + "\n" +
                    "\tДолжность: " + resultSet.getString("post") + "\n\n";
            resultSet = getCandidatesInProcessInfo();
            infoSet += "Сводка по участникам.\n\n";
            while (resultSet.next()) {
                infoSet += "Имя участника: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name") + "\n" +
                        "\tКоманда: " + resultSet.getString("tms.title") == null ? "Без команды" : resultSet.getString("tms.title") + "\n";
            }
            return infoSet;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
