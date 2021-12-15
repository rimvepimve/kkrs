package rs;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import rs.dao.IUserDAO;
import rs.dao.UserDAO;
import rs.model.User;

public class Main extends Application {

    public static User currentUser;
    public static IUserDAO userDAO = new UserDAO();
    public static Stage primary;

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        //FXMLLoader loader = new FXMLLoader(getClass().getResource("../../resources/LogIn.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("LogIn.fxml"));
        Parent root = loader.load();
        Scene primaryScene = new Scene(root, 800, 800);

        primaryScene.getStylesheets().add("css/login.css");
        primaryStage.setTitle("Registration system");
        primaryStage.setScene(primaryScene);
        primary = primaryStage;
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
