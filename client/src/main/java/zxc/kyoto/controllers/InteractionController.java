package zxc.kyoto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import zxc.kyoto.entity.Request;
import zxc.kyoto.util.ClientService;
import zxc.kyoto.util.UserContainer;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

public class InteractionController {
    @FXML
    private TextField interactiomMembers;
    @FXML
    private TextField interactionType;
    @FXML
    private TextField interactionDescription;
    @FXML
    private DatePicker startTime;
    @FXML
    private DatePicker endTime;
    @FXML
    private Label error;

    @FXML
    public void createInteraction(){
        String[] actors = interactiomMembers.getText().split(", ");
        Date start = Date.from(startTime.getValue().atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());;
        Date end = Date.from(endTime.getValue().atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());;
        Request request = new Request(UserContainer.getUser(), new Object[]{"add_interaction", actors, interactionDescription.getText(), interactionType.getText(), start, end});
        String response = ClientService.service(request);
        error.setText(response);
    }

}
