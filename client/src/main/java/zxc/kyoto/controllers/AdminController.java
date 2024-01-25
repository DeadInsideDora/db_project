package zxc.kyoto.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import zxc.kyoto.HelloApplication;
import zxc.kyoto.entity.Request;
import zxc.kyoto.util.ClientService;
import zxc.kyoto.util.StageContainer;
import zxc.kyoto.util.UserContainer;

import java.io.IOException;

public class AdminController {
    @FXML
    private TextArea output;
    @FXML
    private Label error;

    @FXML
    public void startTournament() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin_create_tournament.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void addWriter() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin_add_writer.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void getTournamentInfo() {
        Request request = new Request(UserContainer.getUser(), new String[]{"info_tournament"});
        String response = ClientService.service(request);
        output.setText(response);
    }

    @FXML
    public void getITrialInfo() {
        Request request = new Request(UserContainer.getUser(), new String[]{"info_trial"});
        String response = ClientService.service(request);
        output.setText(response);
    }

    @FXML
    public void addTrial() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin_create_trial.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void addCandidate() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin_add_candidate.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void endTournament() {
        Request request = new Request(UserContainer.getUser(), new String[]{"end_tournament"});
        String response = ClientService.service(request);
        output.setText(response);
    }

    @FXML
    public void addHunters() throws IOException {
        Request request = new Request(UserContainer.getUser(), new String[]{"add_hunter", "name", "fam", "post"});
        String response = ClientService.service(request);
        error.setText(response);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin_add_hunter.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void getHunters(){
        Request request = new Request(UserContainer.getUser(), new String[]{"list_hunters"});
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
