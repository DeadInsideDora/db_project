package zxc.kyoto.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zxc.kyoto.HelloApplication;
import zxc.kyoto.entity.Request;
import zxc.kyoto.entity.User;
import zxc.kyoto.util.ClientService;
import zxc.kyoto.util.Roles;
import zxc.kyoto.util.StageContainer;
import zxc.kyoto.util.UserContainer;

import java.io.IOException;

import static zxc.kyoto.util.Roles.*;

public class AuthController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    private Label error;

    @FXML
    protected void login() {
        String log = username.getText();
        String pass = password.getText();
        String answer = ClientService.service(new Request(null, new String[]{"auth", log, pass}));
        if (answer != "Fail") {
            try {
                User user = new User(log, pass, Roles.getRoleByStr(answer));
                UserContainer.setUser(user);
                switch (user.getRole()) {
                    case VIEWER:
                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view_main.fxml"));
                        Stage stage = StageContainer.mainStage;
                        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
                        stage.setTitle("Viewer");
                        stage.setScene(scene);
                        stage.show();
                        break;
                    case WRITER:
                        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("writer_main.fxml"));
                        stage = StageContainer.mainStage;
                        scene = new Scene(fxmlLoader.load(), 320, 240);
                        stage.setTitle("Writer");
                        stage.setScene(scene);
                        stage.show();
                        break;
                    case ADMIN:
                        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("admin_main.fxml"));
                        stage = StageContainer.mainStage;
                        scene = new Scene(fxmlLoader.load(), 320, 240);
                        stage.setTitle("Admin");
                        stage.setScene(scene);
                        stage.show();
                        break;
                }
            } catch (IllegalArgumentException | IOException e) {
                error.setText(answer);
                System.out.println(answer);
            }
        } else {
            error.setText("User not found.");
        }
    }

    @FXML
    protected void register() {
        ClientService.service(new Request(null, new String[]{"reg", username.getText(), password.getText(), VIEWER.getRole()}));
    }
}
