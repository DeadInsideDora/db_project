package zxc.kyoto.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zxc.kyoto.HelloApplication;
import zxc.kyoto.entity.Request;
import zxc.kyoto.util.ClientService;
import zxc.kyoto.util.UserContainer;

import java.io.IOException;


public class WriterController {

    @FXML
    private TextField updStatus;
    @FXML
    private TextField fname;
    @FXML
    private TextField lname;
    @FXML
    private TextArea info;
    @FXML
    private TextField activateTrial;
    @FXML
    private Label error;

    @FXML
    public void endTrial() {
        Request request = new Request(UserContainer.getUser(), new String[]{"end_trial", activateTrial.getText()});
        String response = ClientService.service(request);
        info.setText(response);
    }

    @FXML
    public void startTrial() {
        Request request = new Request(UserContainer.getUser(), new String[]{"start_trial", activateTrial.getText()});
        String response = ClientService.service(request);
        info.setText(response);
    }

    @FXML
    public void getTrialsList() {
        Request request = new Request(UserContainer.getUser(), new String[]{"trials_list"});
        String response = ClientService.service(request);
        info.setText(response);
    }

    @FXML
    public void getTrialInProcessInfo() {
        Request request = new Request(UserContainer.getUser(), new String[]{"info_trial"});
        String response = ClientService.service(request);
        info.setText(response);
    }

    @FXML
    public void addToTeam() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("writer_create_team.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void createInteraction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("writer_interaction.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void updateCandidateState() {
        Request request = new Request(UserContainer.getUser(), new Object[]{new String[]{fname.getText() + " " + lname.getText()}, new String[]{updStatus.getText()}});
        String response = ClientService.service(request);
        error.setText(response);
    }
}
