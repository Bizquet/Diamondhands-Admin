module com.project.atmadmin {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.project.atmadmin to javafx.fxml;
    opens com.project.atmadmin.datamodel;
    opens com.project.atmadmin.controllers to javafx.fxml;
    exports com.project.atmadmin;
    exports com.project.atmadmin.controllers;
}