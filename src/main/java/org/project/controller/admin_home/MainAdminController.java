package org.project.controller.admin_home;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.project.controller.admin_home.right_side.DashboardController;
import org.project.model.connection.ConnectionStrategy;
import org.project.model.connection.MysqlConnection;
import org.project.model.dao.users.UsersDAOImpl;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainAdminController implements Initializable {
    @FXML
    private VBox rightContentPane;
    @FXML
    private HBox dashboardHbox;
    @FXML
    private HBox usersHbox;
    @FXML
    private HBox announcementHbox;
    @FXML
    private Button startServiceBtn;
    @FXML
    private Button stopServiceBtn;
    private VBox dashboard, users, announcement;
    private double x, y;
    private boolean isMaximized = false;
    private ConnectionStrategy connectionStrategy;
    private UsersDAOImpl usersDAO;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dashboardHbox.setStyle("-fx-background-color:#2A3F54");
        connectionStrategy = MysqlConnection.getInstance();
        try {
            usersDAO = new UsersDAOImpl(connectionStrategy);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/project/views/admin_home/right_side/dashboard_view.fxml"));
            dashboard = loader.load();
            DashboardController dashboardController = loader.<DashboardController>getController();
            dashboardController.setUsersDAO(usersDAO);
            users = FXMLLoader.load(getClass().getResource("/org/project/views/admin_home/right_side/users_view.fxml"));
            announcement = FXMLLoader.load(getClass().getResource("/org/project/views/admin_home/right_side/announcement_view.fxml"));
            setNode(dashboard);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //Set selected node to a content holder
    private void setNode(Node node) {
        rightContentPane.getChildren().clear();
        rightContentPane.getChildren().add((Node) node);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1500));
        fadeTransition.setNode(node);
        fadeTransition.setFromValue(0.1);
        fadeTransition.setToValue(1);
        fadeTransition.setCycleCount(1);
        fadeTransition.setAutoReverse(false);
        fadeTransition.play();
    }

    @FXML
    private void handleSwitchDashboard(ActionEvent event) {
        dashboardHbox.setStyle("-fx-background-color:#2A3F54");
        usersHbox.setStyle("-fx-background-color:#3e3e3e");
        announcementHbox.setStyle("-fx-background-color: #3e3e3e");
        setNode(dashboard);

    }

    @FXML
    private void handleSwitchUsers(ActionEvent event) {
        dashboardHbox.setStyle("-fx-background-color:#3e3e3e");
        usersHbox.setStyle("-fx-background-color:#2A3F54");
        announcementHbox.setStyle("-fx-background-color: #3e3e3e");
        setNode(users);
    }

    @FXML
    private void handleSwitchAnnouncement(ActionEvent event) {
        dashboardHbox.setStyle("-fx-background-color: #3e3e3e");
        usersHbox.setStyle("-fx-background-color: #3e3e3e");
        announcementHbox.setStyle("-fx-background-color:#2A3F54");
        setNode(announcement);
    }

    @FXML
    private void handleStartService(ActionEvent event) {
        startServiceBtn.setDisable(true);
        startServiceBtn.setText("Service Started");
        stopServiceBtn.setDisable(false);
        stopServiceBtn.setText("Stop Service");
    }

    @FXML
    private void handleStopService(ActionEvent event) {
        startServiceBtn.setDisable(false);
        startServiceBtn.setText("Start Service");
        stopServiceBtn.setDisable(true);
        stopServiceBtn.setText("Service Stoped");

    }

    public void handleMinimize(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public void handleMaximize(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setFullScreenExitHint(" ");
        if (!isMaximized) {
            isMaximized = true;
            stage.setMaximized(true);
        } else {
            isMaximized = false;
            stage.setMaximized(false);
        }

    }

    public void handleClose(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void draged(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - x);
        stage.setY(mouseEvent.getScreenY() - y);

    }

    public void pressed(MouseEvent mouseEvent) {
        x = mouseEvent.getSceneX();
        y = mouseEvent.getSceneY();
    }
}
