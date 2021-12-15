package rs.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import rs.dao.AppointmentDAO;
import rs.model.Employee;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static rs.Main.*;

public class MainEmployeeController implements Initializable {

    Employee currentEmployee;
    AppointmentDAO appDAO = new AppointmentDAO();

    @FXML
    private Text txtEmail;

    @FXML
    private Text txtFullName;

    @FXML
    private Text txtPhoneNumber;

    @FXML
    private Text txtSalonName;

    @FXML
    private Text txtSalonAddress;

    @FXML
    private Text txtUserName;

    @FXML
    private Text txtPassword;

    @FXML
    private TextField textFieldEmail;

    @FXML
    private TextField textFieldFullName;

    @FXML
    private TextField textFieldPhoneNumber;

    @FXML
    private TextField textFieldSalonName;

    @FXML
    private TextField textFieldSalonAddress;

    @FXML
    private TextField textFieldUserName;

    @FXML
    private TextField textFieldPassword;

    @FXML
    private Button btnEditInfo;

    @FXML
    private Button btnSaveInfo;

    @FXML
    private Button btnCancel;

    @FXML
    private ListView<String> registeredCustomersList;

    @FXML
    private DatePicker datePickerWorkDay;

    @FXML
    void cancelEdition(ActionEvent event) {

        btnEditInfo.setVisible(true);
        btnSaveInfo.setVisible(false);
        btnCancel.setVisible(false);
        setTextFieldsVisibility(true);
        setEditTextvisibility(false);
    }

    @FXML
    void updateEmployeeInfo(ActionEvent event) {

        userDAO.updateEmployee(textFieldEmail.getText(), textFieldFullName.getText(), textFieldPhoneNumber.getText(),
                textFieldSalonName.getText(), textFieldSalonAddress.getText(), textFieldUserName.getText(),
                textFieldPassword.getText(), currentEmployee.getUserId());

        txtEmail.setText(textFieldEmail.getText());
        txtFullName.setText(textFieldFullName.getText());
        txtPhoneNumber.setText(textFieldPhoneNumber.getText());
        txtSalonName.setText(textFieldSalonName.getText());
        txtSalonAddress.setText(textFieldSalonAddress.getText());
        txtUserName.setText(textFieldUserName.getText());
        txtPassword.setText(textFieldPassword.getText());

        cancelEdition(event);

    }

    @FXML
    void showEditTextFields(ActionEvent event) {

        setTextFieldsVisibility(false);
        setEditTextvisibility(true);
        btnEditInfo.setVisible(false);
        btnSaveInfo.setVisible(true);
        btnCancel.setVisible(true);


    }

    @FXML
    void onDatePickerClicked(MouseEvent event) {
        datePickerWorkDay.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                registeredCustomersList.getItems().clear();
               ArrayList<String> stringsForList = appDAO.getRegistrationsForEmployeeByDate(currentUser.getUserId(),datePickerWorkDay.getValue());
               for(String s: stringsForList){
                   registeredCustomersList.getItems().add(s);
               }
            }
        });

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentEmployee = (Employee)currentUser;
        textFieldEmail.setText(((Employee) currentUser).getEmail());

        txtEmail.setText(currentEmployee.getEmail());
        txtFullName.setText(currentEmployee.getFullName());
        txtPhoneNumber.setText(currentEmployee.getPhoneNumber());
        txtSalonName.setText(currentEmployee.getSalonName());
        txtSalonAddress.setText(currentEmployee.getSalonAddress());
        txtUserName.setText(currentEmployee.getUserName());
        txtPassword.setText(currentEmployee.getPassword());


        textFieldEmail.setText(currentEmployee.getEmail());
        textFieldFullName.setText(currentEmployee.getFullName());
        textFieldPhoneNumber.setText(currentEmployee.getPhoneNumber());
        textFieldSalonName.setText(currentEmployee.getSalonName());
        textFieldSalonAddress.setText(currentEmployee.getSalonAddress());
        textFieldUserName.setText(currentEmployee.getUserName());
        textFieldPassword.setText(currentEmployee.getPassword());

    }

    private void setTextFieldsVisibility(boolean param){
        txtEmail.setVisible(param);
        txtFullName.setVisible(param);
        txtPhoneNumber.setVisible(param);
        txtSalonName.setVisible(param);
        txtSalonAddress.setVisible(param);
        txtUserName.setVisible(param);
        txtPassword.setVisible(param);
    }

    private void setEditTextvisibility(boolean param){
        textFieldEmail.setVisible(param);
        textFieldFullName.setVisible(param);
        textFieldPhoneNumber.setVisible(param);
        textFieldSalonName.setVisible(param);
        textFieldSalonAddress.setVisible(param);
        textFieldUserName.setVisible(param);
        textFieldPassword.setVisible(param);
    }

}
