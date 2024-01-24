package zxc.kyoto.handlers;

import zxc.kyoto.entity.User;
import zxc.kyoto.util.Roles;

import java.sql.ResultSet;

import static zxc.kyoto.dao.DataBaseService.getTournamentInfo;

public class CurrentTrialsListHandler implements Handler{
    @Override
    public String handle(User user, Object[] args) {
        try {
            if (user.getRole() != Roles.ADMIN && user.getRole() != Roles.WRITER)
                throw new IllegalArgumentException("Underprivileged");
            ResultSet resultSet = getTournamentInfo();
            String infoSet = "Список испытаний в турнире:\n";
            while (resultSet.next()) {
                infoSet+= resultSet.getString("tg.description") + "\n ";
            }
            return infoSet;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
