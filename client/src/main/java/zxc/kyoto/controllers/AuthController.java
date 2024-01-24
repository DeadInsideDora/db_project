package zxc.kyoto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    protected void login() {
    }

    @FXML
    protected void register() {
        username.getText();
    }
}
