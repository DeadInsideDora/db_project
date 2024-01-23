package zxc.kyoto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import zxc.kyoto.client.Client;
import zxc.kyoto.entity.User;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        User user =  new User("zxc", "pass", true);
        client.startConnection("127.0.0.1", 4444, user);
        System.out.println(client.sendObject(user));
        client.startConnection("127.0.0.1", 4444, user);
        System.out.println(client.sendObject(user));
        client.startConnection("127.0.0.1", 4444, user);
        System.out.println(client.sendObject(user));
//        launch();
    }
}