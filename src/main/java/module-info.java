module org.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.rmi;

    opens org.project to javafx.fxml;
    exports org.project;
}