package com.project.atmadmin.controllers;

import com.project.atmadmin.datamodel.AdminData;
import com.project.atmadmin.datamodel.ClientInfo;
import com.project.atmadmin.datamodel.Datasource;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class DialogController {

    @FXML
    private TextField fNameField;
    @FXML
    private TextField lNameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField occupationField;

    private int client_uid;

    /**
     * populates the dialog with data from the selected item
     * @param data data object of the selected item
     */
    public void populateDataInfo(AdminData data){
        this.client_uid = data.getClient_uid();
        fNameField.setText(data.getfName());
        lNameField.setText(data.getlName());
        addressField.setText(data.getAddress());
        emailField.setText(data.getEmail());
        occupationField.setText(data.getOccupation());

    }

    /**
     * updates the data with the new inputted ones
     * @return data object that contains the new info
     */
    public AdminData updateData() {
        AdminData newData = new AdminData();


        newData.setfName(fNameField.getText());
        newData.setlName(lNameField.getText());
        newData.setAddress(addressField.getText());
        newData.setEmail(emailField.getText());
        newData.setOccupation(occupationField.getText());

        ClientInfo info = new ClientInfo();
        info.setFirstName(fNameField.getText());
        info.setLastName(lNameField.getText());
        info.setAddress(addressField.getText());
        info.setEmail(emailField.getText());
        info.setOccupation(occupationField.getText());

        Datasource.getInstance().updateClientInfo(client_uid, info);

        return newData;
    }

    /**
     * checks if the textfields has input that will result to errors
     * @return true if has errors, else false
     */
    public boolean hasError(){
        if (fNameField.getText().isEmpty() || lNameField.getText().isEmpty() || addressField.getText().isEmpty() ||
                occupationField.getText().isEmpty() || emailField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("A field is empty!");
            alert.showAndWait();
            return true;
        }
        else if (fNameField.getText().matches(".*\\d.*") || lNameField.getText().matches(".*\\d.*") ||
             occupationField.getText().matches(".*\\d.*")) {

            Alert al = new Alert(Alert.AlertType.ERROR);
            al.setTitle("Error Message");
            al.setHeaderText(null);
            al.setContentText("You input a wrong variable!");
            al.showAndWait();
            return true;
        }

        return false;
    }

}


