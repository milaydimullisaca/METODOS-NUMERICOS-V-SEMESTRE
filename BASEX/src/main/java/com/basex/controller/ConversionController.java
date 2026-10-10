package com.basex.controller;

import com.basex.model.Conversion;
import com.basex.model.BaseNumerica;
import com.basex.service.ConversionService;
import com.basex.service.ValidationService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConversionController {

    private boolean modoFraccionario = false;

    @FXML
    private TextField numeroField;

    @FXML
    private ComboBox<BaseNumerica> baseOrigenCombo;

    @FXML
    private ComboBox<BaseNumerica> baseDestinoCombo;

    @FXML
    private TextArea resultadoArea;
    
    @FXML
    private Button enterosButton;

    @FXML
    private Button fraccionariosButton;
    
    @FXML
    private ListView<String> historialList;

    private final ConversionService conversionService =
            new ConversionService();

    private final ValidationService validationService =
            new ValidationService();

    private final ObservableList<String> historial =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        cargarBases();

        historialList.setItems(historial);

        baseOrigenCombo.getSelectionModel().select(0);
        baseDestinoCombo.getSelectionModel().select(1);

        numeroField.setPromptText(
                "Ejemplo: 101010"
        );
    }

    private void cargarBases() {

        for (int i = 2; i <= 36; i++) {

            String nombre =
                    obtenerNombreBase(i);

            BaseNumerica base =
                    new BaseNumerica(
                            i,
                            nombre
                    );

            baseOrigenCombo.getItems().add(base);
            baseDestinoCombo.getItems().add(base);
        }
    }

    private String obtenerNombreBase(int base) {

        return switch (base) {

            case 2 -> "Binario";
            case 8 -> "Octal";
            case 10 -> "Decimal";
            case 16 -> "Hexadecimal";

            default -> "Base " + base;
        };
    }

    @FXML
    private void convertir() {

        String numero =
                numeroField.getText().trim();

        BaseNumerica origen =
                baseOrigenCombo.getValue();

        BaseNumerica destino =
                baseDestinoCombo.getValue();

        if (numero.isBlank()) {

            mostrarAlerta(
                    "Ingrese un número."
            );

            return;
        }

        if (origen == null || destino == null) {

            mostrarAlerta(
                    "Seleccione las bases."
            );

            return;
        }

        if (!validationService.validarNumero(
                numero,
                origen.getBase(),
                modoFraccionario
        )) {

            mostrarAlerta(
                    validationService.obtenerMensajeError(
                            numero,
                            origen.getBase(),
                            modoFraccionario
                    )
            );

            return;
        }

        try {

            String resultado =
                    conversionService.convertir(
                            numero,
                            origen.getBase(),
                            destino.getBase()
                    );

            resultadoArea.setText(
                    numero
                            + " (Base "
                            + origen.getBase()
                            + ")"
                            + "\n\n↓\n\n"
                            + resultado
                            + " (Base "
                            + destino.getBase()
                            + ")"
            );

            Conversion conversion =
                    new Conversion(
                            numero,
                            origen.getBase(),
                            destino.getBase(),
                            resultado
                    );

            historial.add(
                    conversion.getFechaFormateada()
                            + " | "
                            + numero
                            + " ("
                            + origen.getBase()
                            + ") → "
                            + resultado
                            + " ("
                            + destino.getBase()
                            + ")"
            );

        } catch (Exception e) {

            mostrarAlerta(
                    "No se pudo realizar la conversión.\n\n"
                            + e.getMessage()
            );
        }
    }

    @FXML
    private void convertirATodas() {

        String numero =
                numeroField.getText().trim();

        BaseNumerica origen =
                baseOrigenCombo.getValue();

        if (numero.isBlank()) {

            mostrarAlerta(
                    "Ingrese un número."
            );

            return;
        }

        if (origen == null) {

            mostrarAlerta(
                    "Seleccione la base de origen."
            );

            return;
        }

        if (!validationService.validarNumero(
                numero,
                origen.getBase(),
                modoFraccionario
        )) {

            mostrarAlerta(
                    validationService.obtenerMensajeError(
                            numero,
                            origen.getBase(),
                            modoFraccionario
                    )
            );

            return;
        }

        try {

            String resultado =
                    conversionService.convertirATodas(
                            numero,
                            origen.getBase()
                    );

            resultadoArea.setText(
                    resultado
            );

        } catch (Exception e) {

            mostrarAlerta(
                    "No se pudo realizar la conversión.\n\n"
                            + e.getMessage()
            );
        }
    }

    @FXML
    private void mostrarProcedimiento() {

        String numero =
                numeroField.getText().trim();

        BaseNumerica origen =
                baseOrigenCombo.getValue();

        BaseNumerica destino =
                baseDestinoCombo.getValue();

        if (numero.isBlank()) {

            mostrarAlerta(
                    "Ingrese un número."
            );

            return;
        }

        if (origen == null || destino == null) {

            mostrarAlerta(
                    "Seleccione las bases."
            );

            return;
        }

        if (!validationService.validarNumero(
                numero,
                origen.getBase(),
                modoFraccionario
        )) {

            mostrarAlerta(
                    validationService.obtenerMensajeError(
                            numero,
                            origen.getBase(),
                            modoFraccionario
                    )
            );

            return;
        }

        try {

            String procedimiento =
                    conversionService.obtenerProcedimiento(
                            numero,
                            origen.getBase(),
                            destino.getBase()
                    );

            resultadoArea.setText(
                    procedimiento
            );

        } catch (Exception e) {

            mostrarAlerta(
                    "No se pudo generar el procedimiento.\n\n"
                            + e.getMessage()
            );
        }
    }

    @FXML
    private void limpiar() {

        numeroField.clear();

        resultadoArea.clear();

        baseOrigenCombo
                .getSelectionModel()
                .select(0);

        baseDestinoCombo
                .getSelectionModel()
                .select(1);

        if (modoFraccionario) {

            numeroField.setPromptText(
                    "Ejemplo: 101.101"
            );

        } else {

            numeroField.setPromptText(
                    "Ejemplo: 101010"
            );
        }
    }

    @FXML
    private void limpiarHistorial() {

        historial.clear();
    }

   @FXML
private void modoEnteros() {

    modoFraccionario = false;

    numeroField.clear();

    numeroField.setPromptText(
            "Ejemplo: 101010"
    );

    resultadoArea.clear();

    enterosButton.getStyleClass().remove("secondary-button");

    if (!enterosButton.getStyleClass().contains("primary-button")) {
        enterosButton.getStyleClass().add("primary-button");
    }

    fraccionariosButton.getStyleClass().remove("primary-button");

    if (!fraccionariosButton.getStyleClass().contains("secondary-button")) {
        fraccionariosButton.getStyleClass().add("secondary-button");
    }
}

@FXML
private void modoFraccionarios() {

    modoFraccionario = true;

    numeroField.clear();

    numeroField.setPromptText(
            "Ejemplo: 101.101"
    );

    resultadoArea.clear();

    fraccionariosButton.getStyleClass().remove("secondary-button");

    if (!fraccionariosButton.getStyleClass().contains("primary-button")) {
        fraccionariosButton.getStyleClass().add("primary-button");
    }

    enterosButton.getStyleClass().remove("primary-button");

    if (!enterosButton.getStyleClass().contains("secondary-button")) {
        enterosButton.getStyleClass().add("secondary-button");
    }
}

    @FXML
    private void mostrarAcercaDe() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/com/basex/view/AboutView.fxml"
                            )
                    );

            Scene scene =
                    new Scene(
                            loader.load()
                    );

            String css =
                    getClass()
                            .getResource(
                                    "/com/basex/css/style.css"
                            )
                            .toExternalForm();

            scene.getStylesheets().add(css);

            Stage ventana =
                    new Stage();

            ventana.setTitle(
                    "Acerca de BASEX"
            );

            ventana.setScene(scene);

            ventana.initModality(
                    Modality.APPLICATION_MODAL
            );

            ventana.setResizable(false);

            ventana.showAndWait();

        } catch (Exception e) {

            mostrarAlerta(
                    "No se pudo abrir la ventana "
                            + "Acerca de BASEX.\n\n"
                            + e.getMessage()
            );
        }
    }

    private void mostrarAlerta(
            String mensaje) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.WARNING
                );

        alerta.setTitle(
                "BASEX"
        );

        alerta.setHeaderText(
                null
        );

        alerta.setContentText(
                mensaje
        );

        alerta.showAndWait();
    }
}