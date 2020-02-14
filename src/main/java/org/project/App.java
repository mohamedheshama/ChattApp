package org.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private static void initializeRMI() {
      /*  try {
            ServicesInterface servicesImp = new ServicesImp();
            Registry reg = LocateRegistry.createRegistry(1260);
            reg.rebind("ServerServices", servicesImp);
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void start(Stage stage) throws IOException {
        //scene = new Scene(loadFXML("views/primary"));
        scene = new Scene(loadFXML("views/admin_home/home"));
        stage.setScene(scene);
        stage.setMinWidth(1068);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }


    public static void main(String[] args) {
        initializeRMI();
        launch();

    }

}