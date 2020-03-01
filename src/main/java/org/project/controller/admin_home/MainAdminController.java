package org.project.controller.admin_home;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.project.controller.ServicesImp;
import org.project.controller.ServicesInterface;
import org.project.controller.admin_home.right_side.AnnouncementController;
import org.project.controller.admin_home.right_side.DashboardController;
import org.project.model.connection.ConnectionStrategy;
import org.project.model.connection.MysqlConnection;
import org.project.model.dao.users.UsersDAOImpl;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainAdminController implements Initializable {
    @FXML
    public AnchorPane mainPage;
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
    private Registry reg;
    private ServicesInterface servicesImp;
    private DashboardController dashboardController;
    private FXMLLoader loader;
    private boolean isDashboardScreen = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            System.setProperty("java.rmi.server.hostname", "127.0.0.1"); //10.145.7.12 Uses the loopback address, 127.0.0.1, if yo
            reg = LocateRegistry.createRegistry(1260);
            servicesImp = new ServicesImp(this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        dashboardHbox.setStyle("-fx-background-color:#2A3F54");
        connectionStrategy = MysqlConnection.getInstance();
        try {

            usersDAO = new UsersDAOImpl(connectionStrategy);
            users = FXMLLoader.load(getClass().getResource("/org/project/views/admin_home/right_side/users_view.fxml"));
            loader = new FXMLLoader(getClass().getResource("/org/project/views/admin_home/right_side/announcement_view.fxml"));
            announcement = loader.load();
            AnnouncementController announcementController = loader.getController();
            announcementController.setUsersDAO(usersDAO);
            announcementController.setServicesInterface(servicesImp);
            setNode(getDashboardVbox());
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }

    private VBox getDashboardVbox() {
        isDashboardScreen = true;
        loader = new FXMLLoader(getClass().getResource("/org/project/views/admin_home/right_side/dashboard_view.fxml"));
        try {
            dashboard = loader.load();
            dashboardController = loader.getController();
            dashboardController.setUsersDAO(usersDAO);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dashboard;
    }

    //Set selected node to a content holder
    private void setNode(Node node) {
        rightContentPane.getChildren().clear();
        rightContentPane.getChildren().add(node);
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
        setNode(getDashboardVbox());

    }

    @FXML
    private void handleSwitchUsers(ActionEvent event) {
        isDashboardScreen = false;
        dashboardHbox.setStyle("-fx-background-color:#3e3e3e");
        usersHbox.setStyle("-fx-background-color:#2A3F54");
        announcementHbox.setStyle("-fx-background-color: #3e3e3e");
        setNode(users);
    }

    @FXML
    private void handleSwitchAnnouncement(ActionEvent event) {
        isDashboardScreen = false;
        dashboardHbox.setStyle("-fx-background-color: #3e3e3e");
        usersHbox.setStyle("-fx-background-color: #3e3e3e");
        announcementHbox.setStyle("-fx-background-color:#2A3F54");
        setNode(announcement);
    }

    @FXML
    private void handleStartService(ActionEvent event) throws RemoteException {
        //servicesImp.notifyServerisup();
        startServiceBtn.setDisable(true);
        startServiceBtn.setText("Service Started");
        stopServiceBtn.setDisable(false);
        stopServiceBtn.setText("Stop Service");
        reg.rebind("ServerServices", servicesImp);
        System.out.println("server no is running");
    }

    @FXML
    private void handleStopService(ActionEvent event) throws RemoteException, NotBoundException {
        stopServer();
        startServiceBtn.setDisable(false);
        startServiceBtn.setText("Start Service");
        stopServiceBtn.setDisable(true);
        stopServiceBtn.setText("Service Stoped");
        reg.unbind("ServerServices");
        System.out.println("server now is off");
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
      /*  ((Stage) mainPage.getScene().getWindow()).setOnCloseRequest(windowEvent -> {

        });*/
        stopServer();

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

    public void stopServer() {

        try {
            System.out.println("now closing");
            servicesImp.notifyServerisDown();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void updateDashboard() {
        Platform.runLater(() -> {
            if (isDashboardScreen) {
                setNode(getDashboardVbox());
            }
        });
    }
}
