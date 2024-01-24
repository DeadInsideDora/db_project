package zxc.kyoto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import zxc.kyoto.entity.Request;
import zxc.kyoto.util.ClientService;
import zxc.kyoto.util.UserContainer;

public class AddToTeamController {
    @FXML
    private TextField CandidateLastName;
    @FXML
    private TextField candidateFirstName;
    @FXML
    private TextField teamTitle;
    @FXML
    private Label addMemberError;


    @FXML
    private void addMemberToTeam(){
        Request request = new Request(UserContainer.getUser(), new Object[]{"add_team", teamTitle.getText(), "the BOYS", new String[]{candidateFirstName.getText() + " " + CandidateLastName.getText()}});
        String response = ClientService.service(request);
        addMemberError.setText(response);
    }
}
