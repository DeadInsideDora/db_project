package zxc.kyoto.controllers;

import javafx.fxml.FXML;
import zxc.kyoto.entity.Request;
import zxc.kyoto.util.ClientService;
import zxc.kyoto.util.UserContainer;

public class StartTournamentController {

    @FXML
    public void createNewTournament() {
        Request request = new Request(UserContainer.getUser(), new String[]{"new_tournament"});
        String response = ClientService.service(request);
//        output.setText(response);
    }
}
