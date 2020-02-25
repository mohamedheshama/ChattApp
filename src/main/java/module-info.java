/**
 *
 */
module org.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires java.sql;
    requires java.rmi;
    requires com.jfoenix;
    requires javafx.graphics;
    requires rmiio;
    requires java.desktop;
    opens org.project to javafx.fxml;
    opens org.project.controller.admin_home to javafx.fxml;
    opens org.project.controller.admin_home.right_side to javafx.fxml;
    opens org.project.model.dao.users;
    exports org.project;
    exports org.project.controller.admin_home;
    exports org.project.controller.admin_home.right_side;
    opens org.project.controller;
    exports org.project.model.dao.users;
    exports org.project.model;

}