module org.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.project to javafx.fxml;
    exports org.project;
}