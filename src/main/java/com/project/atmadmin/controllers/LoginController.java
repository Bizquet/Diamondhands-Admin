package com.project.atmadmin.controllers;

import com.project.atmadmin.Main;
import com.project.atmadmin.datamodel.Datasource;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {

    @FXML
    AnchorPane loginPane;
    @FXML
    PasswordField passwordField;

    @FXML
    private void login() throws IOException {
        String password = passwordField.getText();

        //check if password is correct
        boolean hasSimilar = Datasource.getInstance().checkPassword(hash(password));

        if(hasSimilar){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Login Successful!");
            alert.setHeaderText(null);
            alert.setContentText("Please Click Ok to Proceed");
            alert.showAndWait();

            switchToView();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Incorrect Password!");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect Password!!!!!");
            alert.showAndWait();
        }

    }

    /**
     * hashes the string inputted to SHA-256
     * @param pin string tobe hashed
     * @return
     */
    private String hash(String pin){
        byte [] pinHash = null;

        // Encrypt to SHA-256 part just make them both to strings when comparing
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            pinHash = md.digest(pin.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        StringBuilder pinString = new StringBuilder();
        for (int i = 0; i < pinHash.length; i++) {
            if ((0xff & pinHash[i]) < 0x10) {
                pinString.append("0"
                        + Integer.toHexString((0xFF & pinHash[i])));
            } else {
                pinString.append(Integer.toHexString(0xFF & pinHash[i]));
            }
        }

        return pinString.toString();
    }

    /**
     * switches to the view window
     * @throws IOException
     */
    private void switchToView() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("adminview.fxml"));
        Parent root = loader.load();

        ViewController controller = loader.getController();
        controller.initData();

        Stage stage = (Stage) loginPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    }
}
