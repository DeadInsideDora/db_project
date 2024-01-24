package zxc.kyoto.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zxc.kyoto.HelloApplication;
import zxc.kyoto.entity.Request;
import zxc.kyoto.util.ClientService;
import zxc.kyoto.util.StageContainer;
import zxc.kyoto.util.UserContainer;

import java.io.IOException;

public class ViewerController {
    @FXML
    private TextField candidateFirstName;
    @FXML
    private TextField candidateLastName;
    @FXML
    private TextArea output;

    @FXML
    public void getCandidateHistory() {
        Request request = new Request(UserContainer.getUser(), new String[]{"hist_candidate", candidateFirstName.getText(), candidateLastName.getText()});
        String response = ClientService.service(request);
        output.setText(response);
    }

    @FXML
    public void getCandidateStatus() {
        Request request = new Request(UserContainer.getUser(), new String[]{"stat_candidate", candidateFirstName.getText(), candidateLastName.getText()});
        String response = ClientService.service(request);
        output.setText(response);
    }

    @FXML
    public void logOut() throws IOException {
        UserContainer.setUser(null);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view_login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = StageContainer.mainStage;
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
