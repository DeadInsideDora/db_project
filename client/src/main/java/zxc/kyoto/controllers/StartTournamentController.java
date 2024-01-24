package zxc.kyoto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import zxc.kyoto.entity.Request;
import zxc.kyoto.util.ClientService;
import zxc.kyoto.util.UserContainer;

public class StartTournamentController {
    @FXML
    private TextField firstTrial;
    @FXML
    private TextField secondTrial;
    @FXML
    private TextField ThirdTrial;
    @FXML
    private TextField descriptionGpup;

    @FXML
    private Label error;

    @FXML
    public void createNewTournament() {
        Request request = new Request(UserContainer.getUser(), new Object[]{"new_tournament", new String[]{firstTrial.getText(), secondTrial.getText(), ThirdTrial.getText()}, descriptionGpup.getText() });
        String response = ClientService.service(request);
        error.setText(response);
    }
}
