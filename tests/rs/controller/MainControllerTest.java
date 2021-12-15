package rs.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainControllerTest {

  String path = "../view/customerProfile.fxml";

  @Test
  public void showProfile() {
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
    assertTrue(!path.contains(".fxml"));
    assertNotNull(fxmlLoader);
  }
}