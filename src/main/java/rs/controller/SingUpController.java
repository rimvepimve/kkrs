package rs.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import rs.model.Customer;
import rs.model.Employee;

import static rs.Main.userDAO;

public class SingUpController {

  @FXML
  private AnchorPane registerForm;

  @FXML
  private TextField cUsername;

  @FXML
  private PasswordField cPassword;

  @FXML
  private PasswordField cConfirmedPassword;

  @FXML
  private TextField cEmail;

  @FXML
  private TextField cFullName;

  @FXML
  private DatePicker cBirthday;

  @FXML
  private ToggleGroup gender;

  @FXML
  private TextField cPhoneNumber;

  @FXML
  private Text cPwConfirmAlert;

  @FXML
  private TextField eUsername;

  @FXML
  private TextField eEmail;

  @FXML
  private TextField eFullName;

  @FXML
  private TextField ePhoneNumber;

  @FXML
  private DatePicker eBirthday;

  @FXML
  private PasswordField ePassword;

  @FXML
  private PasswordField eConfirmedPassword;

  @FXML
  private TextField eSalonName;

  @FXML
  private TextField eSalonAddress;

  @FXML
  private Text ePwConfirmAlert;

  @FXML
  private Text eRequiredFields;

 @FXML
  private Text cRequiredFields;


  @FXML
  void registerNewCustomer(ActionEvent event) {
    customerDataValidation();
    if (isUserDataValid()) {
      Customer newCustomer = new Customer(
          cUsername.getText(),
          cPassword.getText(),
          cEmail.getText(),
          cFullName.getText(),
          cBirthday.getValue(),
          getGenderFromForm(),
          cPhoneNumber.getText()
      );
      userDAO.addNewCustomer(newCustomer);
      closeCurrentStage();
    }
  }

  @FXML
  void registerNewEmployee(ActionEvent event) {
    employeeDataValidation();
    if (isEmployeeDataValid()) {
      Employee newEmployee = new Employee(
              eUsername.getText(),
              ePassword.getText(),
              eEmail.getText(),
              eFullName.getText(),
              eBirthday.getValue(),
              getGenderFromForm(),
              ePhoneNumber.getText(),
              eSalonName.getText(),
              eSalonAddress.getText()
      );
      userDAO.addNewEmployee(newEmployee);
      closeCurrentStage();
    }
  }

  private void customerDataValidation() {
    if(!cUsername.getText().trim().isEmpty() && !cPassword.getText().trim().isEmpty()
            && !cConfirmedPassword.getText().trim().isEmpty() && !cEmail.getText().trim().isEmpty()
            && !cFullName.getText().trim().isEmpty()) {
      cRequiredFields.setVisible(false);
    } else {
      cRequiredFields.setVisible(true);
    }
    if(cPassword.getText().equals(cConfirmedPassword.getText())) {
      cPwConfirmAlert.setVisible(false);
      cConfirmedPassword.clear();
    } else {
      cPwConfirmAlert.setVisible(true);
    }
  }

  private void employeeDataValidation() {
    if(eUsername.getText().trim().isEmpty() || ePassword.getText().trim().isEmpty()
            || eConfirmedPassword.getText().trim().isEmpty() || eEmail.getText().trim().isEmpty()
            || eFullName.getText().trim().isEmpty() || eSalonAddress.getText().trim().isEmpty()) {
      eRequiredFields.setVisible(true);
    } else {
      eRequiredFields.setVisible(false);
    }
    if(!ePassword.getText().equals(eConfirmedPassword.getText())) {
      ePwConfirmAlert.setVisible(true);
      eConfirmedPassword.clear();
    } else {
      ePwConfirmAlert.setVisible(false);
    }
  }

  private boolean isUserDataValid(){
    return !cRequiredFields.isVisible() && !cPwConfirmAlert.isVisible();
  }

  private boolean isEmployeeDataValid(){
    return !eRequiredFields.isVisible() && !ePwConfirmAlert.isVisible();
  }

  private void closeCurrentStage() {
    Stage currentStage = (Stage) registerForm.getScene().getWindow();
    currentStage.close();
  }

  private String getGenderFromForm(){
    RadioButton selectedRadioBtn = (RadioButton) (gender.getSelectedToggle());
    return gender.getSelectedToggle()== null ? "" : selectedRadioBtn.getText().substring(0,1);
  }

}
