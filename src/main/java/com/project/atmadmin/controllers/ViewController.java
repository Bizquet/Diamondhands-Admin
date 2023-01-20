package com.project.atmadmin.controllers;

import com.project.atmadmin.Main;
import com.project.atmadmin.datamodel.AdminData;
import com.project.atmadmin.datamodel.Datasource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class ViewController {

    @FXML
    private TableView adminTable;
    @FXML
    private AnchorPane viewPanel;

    public void initialize(){}

    /**
     * binds the data before calling the scene
     */
    public void initData(){
        Task<ObservableList<AdminData>> task = new GetAllAdminData();

        adminTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();

    }

    /**
     * shows the edit dialog where data of the selected data is displayed. It can be edited
     */
    @FXML
    private void showEditDialog(){
        final AdminData selectedData = (AdminData) adminTable.getSelectionModel().getSelectedItem();

        if(selectedData == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Data Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select data you want edit");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(viewPanel.getScene().getWindow());
        dialog.setTitle("Edit Data");
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("editDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e){
            System.out.println("Couldn't load dialog");
            e.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        DialogController controller = fxmlLoader.getController();
        controller.populateDataInfo(selectedData);


        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            if(!controller.hasError()){
                System.out.println(!controller.hasError());
                AdminData newData = controller.updateData();
                selectedData.setfName(newData.getfName());
                selectedData.setlName(newData.getlName());
                selectedData.setAddress(newData.getAddress());
                selectedData.setEmail(newData.getEmail());
                selectedData.setOccupation(newData.getOccupation());
            }
        }

    }

    /**
     * Deletes the current selected data
     */
    @FXML
    private void deleteData(){
        final AdminData selectedData = (AdminData) adminTable.getSelectionModel().getSelectedItem();

        if(selectedData == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Data Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select data you want delete");
            alert.showAndWait();
            return;
        }

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return Datasource.getInstance().deleteClientInfo(selectedData.getClient_uid());
            }
        };

        task.setOnSucceeded(e -> {
            adminTable.getItems().remove(selectedData);
            adminTable.refresh();
        });

        task.setOnFailed(e ->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Error Deleting Data");
            alert.setHeaderText(null);
            alert.setContentText("There was a problem in deleting selected data");
            alert.showAndWait();
        });

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Contact");
        alert.setContentText("Are yous sure you want to delete the selected contact? " +
                selectedData.getfName() + " " + selectedData.getlName());

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){
            new Thread(task).start();
        }
    }

    /**
     * switches to the loginwindow
     * @throws IOException
     */
    @FXML
    private void logout() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("adminlogin.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) viewPanel.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


}

/**
 * class task that retrieves all data from the view and returns it as an observableArrayList
 */
class GetAllAdminData extends Task{

    @Override
    protected ObservableList<AdminData> call() {
        return FXCollections.observableArrayList(Datasource.getInstance().queryAdminData());
    }
}


