package rs.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import rs.dao.UserDAO;
import rs.model.User;

import java.net.URL;

import static rs.Main.userDAO;
import static rs.Main.currentUser;


public class LoginController {

  @FXML private TextField username;

  @FXML private PasswordField password;

  @FXML
  void signIn(ActionEvent event) {
    if (isUserVierified()) {
      goToMainWindow(event);
    } else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText("Failed to log in");
      alert.setContentText("Incorrect username or password. Try again.");
      alert.showAndWait();
      username.clear();
      password.clear();
    }
  }

  @FXML
  void loadNewUserRegistrationForm(ActionEvent event) {
    try {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/signUp.fxml"));
      Parent root1 = fxmlLoader.load();
      Stage stage1 = new Stage();
      stage1.setTitle("User Registration Form");
      stage1.setScene(new Scene(root1, 600, 600));
      stage1.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private boolean isUserVierified(){
    boolean verified = false;
    UserDAO users = new UserDAO();
    for(User user: users.getAllUsers()) {
      if(user.getUserName().equals(username.getText()) && user.getPassword().equals(password.getText())) {
        verified = true;
        currentUser = user;
        break;
      }
    }
    return verified;
  }

  private void goToMainWindow(ActionEvent event) {
    String resource = "/main.fxml";
    if (currentUser.getUserType().equals("e")) {
      resource = "/mainEmployee.fxml";
    }
    URL fxmlLocation = getClass().getResource(resource);
    try {
      Parent rootLoggedIn = FXMLLoader.load(fxmlLocation);
      Stage stageLoggedIn = (Stage) ((Button) event.getSource()).getScene().getWindow();
      stageLoggedIn.setTitle("Registration System");
      stageLoggedIn.setScene(new Scene(rootLoggedIn, 800, 800));
      stageLoggedIn.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
