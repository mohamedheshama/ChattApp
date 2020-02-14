module org.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.rmi;

    opens org.project to javafx.fxml;
    opens org.project.controller;
    exports org.project;
}