package zxc.kyoto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import zxc.kyoto.entity.Request;
import zxc.kyoto.util.UserContainer;

public class CreateTrialController {
    @FXML
    private TextField newTrial;
    @FXML
    private TextField organizators;
    @FXML
    private TextField rules;

    @FXML
    public void createTrial() {
        String[] orgs = organizators.getText().split(", ");
        String[] rls = rules.getText().split(", ");
        Request request = new Request(UserContainer.getUser(), new Object[]{"add_trial",})
    }
}
