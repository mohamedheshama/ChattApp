package org.project;

import javafx.fxml.FXML;

import java.io.IOException;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("views/primary");
        System.out.println("dslkfngjoind");
    }
}