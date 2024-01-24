package zxc.kyoto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import zxc.kyoto.entity.Request;
import zxc.kyoto.util.ClientService;
import zxc.kyoto.util.UserContainer;

public class AddCandidateController {
    @FXML
    private TextField firstNameNewCandidate;
    @FXML
    private TextField lastNameNewCandidate;
    @FXML
    private TextField facts;
    @FXML
    private TextField weights;
    @FXML
    private Label error;

    @FXML
    public void addCandidate() {
        String[] parsedFacts = facts.getText().split(", ");
        String[] parsedWeights = weights.getText().split(", ");
        Request request = new Request(UserContainer.getUser(), new Object[]{"add_candidate", firstNameNewCandidate.getText(), lastNameNewCandidate.getText(), parsedFacts, parsedWeights});
        String response = ClientService.service(request);
        error.setText(response);
    }
}
