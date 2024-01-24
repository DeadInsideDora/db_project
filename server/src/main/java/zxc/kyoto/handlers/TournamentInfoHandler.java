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
            ResultSet resultSet = getTournamentInfo();
            String infoSet = "Сводка по турниру:\n\n";
            while (resultSet.next()) {
                infoSet+= "Турнир: " + resultSet.getString("tg.description") + "\n " +
                        "Имя участника: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name") + "\n" +
                        "Команда: " + resultSet.getString("tms.title") == null ? "Без команды" : resultSet.getString("tms.title") + "\n" +
                        "Испытание: " + resultSet.getString("t.title") + "\n" +
                        "\tОписание: " + resultSet.getString("t.description") + "\n" +
                        "Статус: " + resultSet.getString("s.description") + "\n\n";
            }
            return infoSet;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
