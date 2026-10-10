package com.basex.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AboutController {

    @FXML
    private Label versionLabel;

    @FXML
    public void initialize() {

        versionLabel.setText("Versión 1.0");
    }

    @FXML
    private void cerrar() {

        Stage ventana =
                (Stage) versionLabel
                        .getScene()
                        .getWindow();

        ventana.close();
    }
}