module org.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.rmi;
    requires com.jfoenix;
    requires javafx.graphics;

    opens org.project to javafx.fxml;
    opens org.project.controller;
    exports org.project;


}