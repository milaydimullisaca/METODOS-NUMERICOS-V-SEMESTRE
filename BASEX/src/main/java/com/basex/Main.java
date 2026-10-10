package com.basex;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/basex/view/MainView.fxml"
                )
        );

        Scene scene = new Scene(loader.load());

        scene.getStylesheets().add(
                getClass().getResource(
                        "/com/basex/css/style.css"
                ).toExternalForm()
        );

        stage.setTitle(
                "BASEX - Conversor Universal de Sistemas de Numeración"
        );

        stage.getIcons().add(
                new Image(
                        getClass().getResourceAsStream(
                                "/com/basex/images/icon.png"
                        )
                )
        );

        stage.setScene(scene);

        stage.setMinWidth(900);
        stage.setMinHeight(650);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}