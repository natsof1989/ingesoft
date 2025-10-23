module com.mycompany.ingesoft {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    
    opens com.mycompany.ingesoft to javafx.fxml;
    opens com.mycompany.ingesoft.controllers to javafx.fxml;
    
    exports com.mycompany.ingesoft;
    exports com.mycompany.ingesoft.controllers;
    exports com.mycompany.ingesoft.dao;
    exports com.mycompany.ingesoft.models;
}
