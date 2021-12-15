package rs.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import rs.dao.AppointmentDAO;
import rs.model.Customer;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static rs.Main.*;

public class CustomerProfileController implements Initializable {

    Customer customer = (Customer) currentUser;
    AppointmentDAO appDAO = new AppointmentDAO();

    @FXML
    private ListView<String> registeredCustomersList;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldFullName;

    @FXML
    private TextField textFieldPhoneNumber;

    @FXML
    private TextField textFieldUserName;

    @FXML
    private TextField textFieldPassword;

    @FXML
    private Button btnSaveInfo;

    @FXML
    private Button btnEditInfo;

    @FXML
    private Text txtEmail;

    @FXML
    private Text txtFullName;

    @FXML
    private Text txtPhoneNumber;

    @FXML
    private Text txtUserName;

    @FXML
    private Text txtPassword;

    @FXML
    private Button btnCancel;

    @FXML
    void cancelEdition(ActionEvent event) {

        btnEditInfo.setDisable(false);
        btnSaveInfo.setDisable(true);
        btnCancel.setDisable(true);
        setTextFieldsVisibility(true);
        setEditTextVisibility(false);

    }

    @FXML
    void deleteAccount(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Do you want to delete your profile?");
        alert.setContentText("Your profile will be permanently deleted.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            userDAO.delete(currentUser.getUserId());
            signOut(event);
        }
    }

    @FXML
    void showEditTextFields(ActionEvent event) {

        setTextFieldsVisibility(false);
        setEditTextVisibility(true);
        btnEditInfo.setDisable(true);
        btnSaveInfo.setDisable(false);
        btnCancel.setDisable(false);

    }

    @FXML
    void signOut(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/LogIn.fxml"));
            Stage primaryStage = primary;
            Scene primaryScene = new Scene(root, 800, 800);
            primaryScene.getStylesheets().add("css/login.css");
            primaryStage.setTitle("Sign In");
            primaryStage.setScene(primaryScene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void updateCustomerInfo(ActionEvent event) {

        userDAO.updateCustomer(textFieldEmail.getText(), textFieldFullName.getText(), textFieldPhoneNumber.getText(),
                textFieldUserName.getText(),textFieldPassword.getText(), currentUser.getUserId());

        txtEmail.setText(textFieldEmail.getText());
        txtFullName.setText(textFieldFullName.getText());
        txtPhoneNumber.setText(textFieldPhoneNumber.getText());
        txtUserName.setText(textFieldUserName.getText());
        txtPassword.setText(textFieldPassword.getText());

        cancelEdition(event);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ArrayList<String> stringsForList = appDAO.getCustomerRegistrations(currentUser.getUserId());
        for(String s: stringsForList){
            registeredCustomersList.getItems().add(s);
        }

        txtEmail.setText(customer.getEmail());
        txtFullName.setText(customer.getFullName());
        txtPhoneNumber.setText(customer.getPhoneNumber());
        txtUserName.setText(customer.getUserName());
        txtPassword.setText(customer.getPassword());

        textFieldEmail.setText(customer.getEmail());
        textFieldFullName.setText(customer.getFullName());
        textFieldPhoneNumber.setText(customer.getPhoneNumber());
        textFieldUserName.setText(customer.getUserName());
        textFieldPassword.setText(customer.getPassword());

    }

    private void setTextFieldsVisibility(boolean param){
        txtEmail.setVisible(param);
        txtFullName.setVisible(param);
        txtPhoneNumber.setVisible(param);
        txtUserName.setVisible(param);
        txtPassword.setVisible(param);
    }

    private void setEditTextVisibility(boolean param){
        textFieldEmail.setVisible(param);
        textFieldFullName.setVisible(param);
        textFieldPhoneNumber.setVisible(param);
        textFieldUserName.setVisible(param);
        textFieldPassword.setVisible(param);
    }

}
