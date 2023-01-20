package com.project.atmadmin.datamodel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AdminData {

    private SimpleIntegerProperty client_uid = new SimpleIntegerProperty();
    private SimpleIntegerProperty acc_uid = new SimpleIntegerProperty();
    private SimpleStringProperty fName = new SimpleStringProperty("");
    private SimpleStringProperty lName = new SimpleStringProperty("");
    private SimpleStringProperty address = new SimpleStringProperty("");
    private SimpleStringProperty email = new SimpleStringProperty("");
    private SimpleStringProperty occupation = new SimpleStringProperty("");
    private SimpleStringProperty acc_type = new SimpleStringProperty("");

    public AdminData(int client_uid, int acc_uid, String fName, String lName, String address, String email,
                     String occupation, String acc_type) {
        this.client_uid.setValue(client_uid);
        this.acc_uid.setValue(acc_uid);
        this.fName.set(fName);
        this.lName.set(lName);
        this.address.set(address);
        this.email.set(email);
        this.occupation.set(occupation);
        this.acc_type.set(acc_type);
    }

    public AdminData() {
    }

    public int getClient_uid() {
        return client_uid.get();
    }

    public SimpleIntegerProperty client_uidProperty() {
        return client_uid;
    }

    public void setClient_uid(int client_uid) {
        this.client_uid.set(client_uid);
    }

    public int getAcc_uid() {
        return acc_uid.get();
    }

    public SimpleIntegerProperty acc_uidProperty() {
        return acc_uid;
    }

    public void setAcc_uid(int acc_uid) {
        this.acc_uid.set(acc_uid);
    }

    public String getfName() {
        return fName.get();
    }

    public SimpleStringProperty fNameProperty() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName.set(fName);
    }

    public String getlName() {
        return lName.get();
    }

    public SimpleStringProperty lNameProperty() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName.set(lName);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getOccupation() {
        return occupation.get();
    }

    public SimpleStringProperty occupationProperty() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation.set(occupation);
    }

    public String getAcc_type() {
        return acc_type.get();
    }

    public SimpleStringProperty acc_typeProperty() {
        return acc_type;
    }

    public void setAcc_type(String acc_type) {
        this.acc_type.set(acc_type);
    }
}
