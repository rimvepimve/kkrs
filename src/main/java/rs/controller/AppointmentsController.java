package rs.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import rs.dao.AppointmentDAO;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static rs.Main.currentUser;

public class AppointmentsController implements Initializable {

    private int employee_id;
    private AppointmentDAO appDAO = new AppointmentDAO();


    @FXML
    private DatePicker registrationDatePicker;

    @FXML
    private ComboBox<String> visitTimeCombo;

    @FXML
    void onDatePickerClicked(MouseEvent event) {
        visitTimeCombo.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                LocalDate date = registrationDatePicker.getValue();
                LocalDate now = LocalDate.now();
                int datesComparision = date.compareTo(now);
                if (datesComparision < 0 || datesComparision > 30) {
                    popupAlert();
                    registrationDatePicker.setValue(now);
                }
            }
        });
    }

    @FXML
    void onTimesComboClicked(MouseEvent event) {
        if(registrationDatePicker.getValue() != null) {
            ArrayList<String> possibleTimes = getVisitTimes();
            ObservableList<String> data = FXCollections.observableArrayList(possibleTimes);
            visitTimeCombo.setItems(data);
        }
    }

    @FXML
    void doRegistration(ActionEvent event) {
        int user_id = currentUser.getUserId();
        LocalDate date = registrationDatePicker.getValue();
        LocalDate now = LocalDate.now();
        int datesComparision = date.compareTo(now);
        if (datesComparision < 0 || datesComparision > 30) {
            popupAlert();
            registrationDatePicker.setValue(now);
            return;
        }
        if ("Chose visit time".equals(visitTimeCombo.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Chose visit time");
            alert.showAndWait();
        }
        String hour = visitTimeCombo.getValue();
        appDAO.addAppointment(employee_id, user_id, date, hour);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText("Successfully registered");
        alert.showAndWait();

        closeRegistrationWindow(event);

    }

    private void closeRegistrationWindow(ActionEvent event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    private void popupAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Data");
        alert.setContentText("Date must be within 30 days of today");
        alert.showAndWait();
    }

    private ArrayList<String> getVisitTimes() {
        LocalDate date = registrationDatePicker.getValue();
        ArrayList<String> occupiedTimes = appDAO.getOccupiedTimes(employee_id, date);
        ArrayList<String> times = new ArrayList<>();
        times.add(" 8:00");
        times.add(" 9:00");
        times.add("10:00");
        times.add("11:00");
        times.add("13:00");
        times.add("14:00");
        times.add("15:00");
        times.add("16:00");
        times.add("17:00");
        times.add("18:00");
        times.add("19:00");

        if (occupiedTimes == null) {
            return times;
        }

        for (int i=0; i<occupiedTimes.size(); i++) {
            for (int j=times.size()-1; j>=0; j--) {
                if (occupiedTimes.get(i).equals(times.get(j))) {
                    times.remove(j);
                }
            }
        }

        return times;
    }

}
