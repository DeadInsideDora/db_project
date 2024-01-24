package zxc.kyoto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import zxc.kyoto.entity.Request;
import zxc.kyoto.util.ClientService;
import zxc.kyoto.util.UserContainer;

public class ViewerController {
    @FXML
    private TextField candidateFirstName;
    @FXML
    private TextField candidateLastName;
    @FXML
    private TextArea output;

    @FXML
    public void getCandidateHistory(){

    }

    @FXML
    public void getCandidateStatus() {
        Request request = new Request(UserContainer.getUser(), new String[]{"stat_candidate", candidateFirstName.getText(), candidateLastName.getText()});
        String response = ClientService.service(request);
        output.setText(response);
    }
}
