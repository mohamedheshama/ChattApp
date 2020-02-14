module org.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.rmi;
    requires com.jfoenix;
    requires javafx.graphics;

    opens org.project to javafx.fxml;
    opens org.project.Controller.admin_home to javafx.fxml;
    opens org.project.Controller.admin_home.right_side to javafx.fxml;
    exports org.project;
    exports org.project.Controller.admin_home;
    exports org.project.Controller.admin_home.right_side;

}