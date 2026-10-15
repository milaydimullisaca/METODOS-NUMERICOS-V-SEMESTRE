
package com.interpolacionlineal;

import com.interpolacionlineal.controlador.ControladorInterpolacion;
import com.interpolacionlineal.vista.VistaPrincipal;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        VistaPrincipal vista = new VistaPrincipal();
        ControladorInterpolacion controlador =
                new ControladorInterpolacion(vista);

        Scene escena = new Scene(vista.getRoot(), 1320, 850);

        var css = getClass().getResource("/styles.css");

        if (css != null) {
            escena.getStylesheets().add(css.toExternalForm());
        }

        stage.setTitle("Interpolación Lineal | Análisis Numérico");
        stage.setMinWidth(1050);
        stage.setMinHeight(720);
        stage.setScene(escena);
        stage.show();

        controlador.inicializar();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
