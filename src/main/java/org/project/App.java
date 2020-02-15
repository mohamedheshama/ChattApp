package org.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.project.controller.ServicesImp;
import org.project.controller.ServicesInterface;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("/org/project/views/admin_home/home"));
        stage.setScene(scene);
        stage.setMinWidth(1068);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private static void initializeRMI(){
        try {
            Registry reg = LocateRegistry.createRegistry(1260);
            System.setProperty("java.rmi.server.hostname", "127.0.0.1"); // Uses the loopback address, 127.0.0.1, if yo
            ServicesInterface servicesImp = new ServicesImp();
            reg.rebind("ServerServices", servicesImp);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        initializeRMI();
        launch();

    }

}