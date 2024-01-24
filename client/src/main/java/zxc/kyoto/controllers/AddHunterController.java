package zxc.kyoto.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zxc.kyoto.HelloApplication;
import zxc.kyoto.entity.Request;
import zxc.kyoto.util.ClientService;
import zxc.kyoto.util.UserContainer;

import java.io.IOException;

public class AddHunterController {
    @FXML
    private TextField firstNameNewCandidate;
    @FXML
    private TextField lastNameNewCandidate;
    @FXML
    private TextField position;
    @FXML
    private Label error;

    @FXML
    public void addHunter() throws IOException {
        Request request = new Request(UserContainer.getUser(), new String[]{"add_hunter", firstNameNewCandidate.getText(), lastNameNewCandidate.getText(), position.getText()});
        String response = ClientService.service(request);
        error.setText(response);
    }
}
