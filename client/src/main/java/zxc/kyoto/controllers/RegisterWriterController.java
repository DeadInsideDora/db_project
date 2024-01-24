package zxc.kyoto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import zxc.kyoto.entity.Request;
import zxc.kyoto.util.ClientService;
import zxc.kyoto.util.UserContainer;

import static zxc.kyoto.util.Roles.WRITER;

public class RegisterWriterController {
    @FXML
    private TextField viewUsername;
    @FXML
    private PasswordField viewPassword;
    @FXML
    private Label error;

    @FXML
    public void registerWriter() {
        Request request = new Request(UserContainer.getUser(), new String[]{"reg", viewUsername.getText(), viewPassword.getText(), WRITER.getRole()});
        String response = ClientService.service(request);
        error.setText(response);
    }
}
