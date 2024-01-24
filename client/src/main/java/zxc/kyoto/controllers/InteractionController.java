package zxc.kyoto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import zxc.kyoto.entity.Request;
import zxc.kyoto.util.ClientService;
import zxc.kyoto.util.UserContainer;

import java.time.Instant;
import java.util.Date;

public class InteractionController {
    @FXML
    private TextField interactiomMembers;
    @FXML
    private TextField interactionType;
    @FXML
    private DatePicker startTime;
    @FXML
    private DatePicker endTime;

    @FXML
    public void createInteraction(){
        String[] actors = interactiomMembers.getText().split(", ");
        Date start = Date.from(Instant.from(startTime.getValue()));
        Date end = Date.from(Instant.from(endTime.getValue()));
        Request request = new Request(UserContainer.getUser(), new Object[]{"add_interaction", actors, "the boys", interactionType.getText(), start, end});
        String response = ClientService.service(request);
    }

}
