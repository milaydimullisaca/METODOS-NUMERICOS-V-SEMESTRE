
package com.interpolacionlineal.vista;

import com.interpolacionlineal.modelo.InterpolacionLineal;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.DoubleUnaryOperator;

public class VistaPrincipal {

    private final BorderPane root = new BorderPane();

    private final TextField campoX1 = crearCampo("2");
    private final TextField campoY1 = crearCampo("ln(2)");
    private final TextField campoX2 = crearCampo("4");
    private final TextField campoY2 = crearCampo("ln(4)");
    private final TextField campoX = crearCampo("3");
    private final TextField campoFuncion = crearCampo("ln(x)");

    private final Label etiquetaFormula = new Label(
            "y = y₁ + [(y₂ − y₁) / (x₂ − x₁)] (x − x₁)");

    private final Label etiquetaPendiente = new Label("Pendiente: —");
    private final Label etiquetaSustitucion = new Label("—");
    private final Label etiquetaResultado = new Label("—");
    private final Label etiquetaValorReal = new Label("Valor real: —");
    private final Label etiquetaErrorAbsoluto =
            new Label("Error absoluto: —");
    private final Label etiquetaErrorRelativo =
            new Label("Error relativo: —");
    private final Label etiquetaCota = new Label("Cota teórica: —");

    private final Canvas grafica = new Canvas(760, 390);

    private final Button botonCalcular =
            new Button("Calcular resultado");
    private final Button botonLimpiar =
            new Button("Restablecer valores");

    public VistaPrincipal() {
        construirInterfaz();
        limpiarResultados();
    }

    private void construirInterfaz() {
        Label marca = new Label("M");
        marca.getStyleClass().add("brand-mark");

        Label titulo = new Label("Interpolación Lineal");
        titulo.getStyleClass().add("main-title");

        Label subtitulo = new Label(
                "Herramienta de análisis numérico");
        subtitulo.getStyleClass().add("subtitle");

        VBox textoEncabezado = new VBox(4, titulo, subtitulo);

        HBox encabezado = new HBox(15, marca, textoEncabezado);
        encabezado.setAlignment(Pos.CENTER_LEFT);
        encabezado.getStyleClass().add("top-header");

        VBox panelDatos = crearPanelDatos();
        VBox panelGrafica = crearPanelGrafica();
        VBox panelSolucion = crearPanelSolucion();

        VBox zonaDerecha = new VBox(18, panelGrafica, panelSolucion);
        zonaDerecha.setFillWidth(true);
        HBox.setHgrow(zonaDerecha, Priority.ALWAYS);

        HBox contenido = new HBox(20, panelDatos, zonaDerecha);
        contenido.setPadding(new Insets(24));
        contenido.setAlignment(Pos.TOP_LEFT);
        contenido.setFillHeight(true);

        ScrollPane scroll = new ScrollPane(contenido);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(false);
        scroll.getStyleClass().add("main-scroll");

        root.setTop(encabezado);
        root.setCenter(scroll);
        root.getStyleClass().add("app-background");
    }

    private VBox crearPanelDatos() {
        Label titulo = new Label("Datos de entrada");
        titulo.getStyleClass().add("section-title");

        Label descripcion = new Label(
                "Introduce los puntos conocidos y el valor que deseas calcular.");
        descripcion.getStyleClass().add("muted-text");
        descripcion.setWrapText(true);

        VBox puntoUno = crearGrupoPunto(
                "PUNTO 01", "x₁", campoX1, "y₁", campoY1);

        VBox puntoDos = crearGrupoPunto(
                "PUNTO 02", "x₂", campoX2, "y₂", campoY2);

        Label etiquetaX = etiquetaCampo("Valor de x");
        Label ayudaX = new Label("Debe estar entre x₁ y x₂.");
        ayudaX.getStyleClass().add("muted-text");

        VBox grupoX = new VBox(8, etiquetaX, campoX, ayudaX);
        grupoX.getStyleClass().add("field-group");

        Label etiquetaFuncion = etiquetaCampo("Función original f(x)");
        Label ayudaFuncion = new Label(
                "Se utiliza para calcular el error real.");
        ayudaFuncion.getStyleClass().add("muted-text");

        VBox grupoFuncion = new VBox(
                8, etiquetaFuncion, campoFuncion, ayudaFuncion);
        grupoFuncion.getStyleClass().add("field-group");

        botonCalcular.getStyleClass().add("primary-button");
        botonCalcular.setMaxWidth(Double.MAX_VALUE);

        botonLimpiar.getStyleClass().add("secondary-button");
        botonLimpiar.setMaxWidth(Double.MAX_VALUE);

        VBox panel = new VBox(
                18,
                titulo,
                descripcion,
                puntoUno,
                puntoDos,
                grupoX,
                grupoFuncion,
                botonCalcular,
                botonLimpiar
        );

        panel.setPrefWidth(300);
        panel.setMinWidth(270);
        panel.getStyleClass().add("card");

        return panel;
    }

    private VBox crearGrupoPunto(
            String titulo,
            String nombreX,
            TextField x,
            String nombreY,
            TextField y) {

        Label encabezado = new Label(titulo);
        encabezado.getStyleClass().add("group-heading");

        GridPane campos = new GridPane();
        campos.setHgap(10);
        campos.setVgap(8);

        campos.add(etiquetaCampo(nombreX), 0, 0);
        campos.add(etiquetaCampo(nombreY), 1, 0);
        campos.add(x, 0, 1);
        campos.add(y, 1, 1);

        VBox grupo = new VBox(12, encabezado, campos);
        grupo.getStyleClass().add("point-group");

        return grupo;
    }

    private VBox crearPanelGrafica() {
        Label titulo = new Label("Representación gráfica");
        titulo.getStyleClass().add("section-title");

        Label descripcion = new Label(
                "Puntos conocidos, recta interpolante y valor aproximado.");
        descripcion.getStyleClass().add("muted-text");
        descripcion.setWrapText(true);

        grafica.setWidth(760);
        grafica.setHeight(390);

        HBox leyenda = new HBox(
                18,
                elementoLeyenda("#2563EB", "Recta interpolante"),
                elementoLeyenda("#16A34A", "Punto interpolado"),
                elementoLeyenda("#94A3B8", "Líneas auxiliares")
        );

        leyenda.setAlignment(Pos.CENTER_LEFT);

        VBox panel = new VBox(
                14, titulo, descripcion, grafica, leyenda);
        panel.getStyleClass().add("card");
        panel.setMaxWidth(Double.MAX_VALUE);

        return panel;
    }

    private HBox elementoLeyenda(String color, String texto) {
        Label punto = new Label("●");
        punto.setStyle(
                "-fx-text-fill: " + color + "; -fx-font-size: 15px;");

        Label nombre = new Label(texto);
        nombre.getStyleClass().add("legend-text");

        return new HBox(7, punto, nombre);
    }

    private VBox crearPanelSolucion() {
        Label titulo = new Label("Desarrollo matemático");
        titulo.getStyleClass().add("section-title");

        Label formulaTitulo = encabezadoGrupo("FÓRMULA GENERAL");
        etiquetaFormula.getStyleClass().add("formula-text");
        etiquetaFormula.setWrapText(true);

        etiquetaPendiente.getStyleClass().add("step-text");

        Label sustitucionTitulo =
                encabezadoGrupo("SUSTITUCIÓN Y CÁLCULO");
        etiquetaSustitucion.getStyleClass().add("step-text");
        etiquetaSustitucion.setWrapText(true);

        Label resultadoTitulo = encabezadoGrupo("RESULTADO FINAL");
        etiquetaResultado.getStyleClass().add("result-value");

        VBox resultadoCard = new VBox(
                8, resultadoTitulo, etiquetaResultado);
        resultadoCard.getStyleClass().add("result-card");

        Label errorTitulo = encabezadoGrupo("ANÁLISIS DEL ERROR");

        etiquetaValorReal.getStyleClass().add("step-text");
        etiquetaErrorAbsoluto.getStyleClass().add("step-text");
        etiquetaErrorRelativo.getStyleClass().add("step-text");
        etiquetaCota.getStyleClass().add("step-text");

        VBox panelError = new VBox(
                10,
                errorTitulo,
                etiquetaValorReal,
                etiquetaErrorAbsoluto,
                etiquetaErrorRelativo,
                etiquetaCota
        );

        panelError.getStyleClass().add("point-group");

        VBox panel = new VBox(
                15,
                titulo,
                formulaTitulo,
                etiquetaFormula,
                etiquetaPendiente,
                sustitucionTitulo,
                etiquetaSustitucion,
                resultadoCard,
                panelError
        );

        panel.getStyleClass().add("card");
        return panel;
    }

    private Label etiquetaCampo(String texto) {
        Label label = new Label(texto);
        label.getStyleClass().add("field-label");
        return label;
    }

    private Label encabezadoGrupo(String texto) {
        Label label = new Label(texto);
        label.getStyleClass().add("group-heading");
        return label;
    }

    private TextField crearCampo(String valor) {
        TextField campo = new TextField(valor);
        campo.getStyleClass().add("input-field");
        campo.setPrefWidth(115);
        return campo;
    }

    public void mostrarResultados(
            InterpolacionLineal modelo,
            double x,
            double y,
            double valorReal,
            double errorAbsoluto,
            double errorRelativo,
            double cotaTeorica) {

        etiquetaFormula.setText(
                "y = " + f(modelo.getY1()) + " + [("
                + f(modelo.getY2()) + " − " + f(modelo.getY1())
                + ") / (" + f(modelo.getX2()) + " − "
                + f(modelo.getX1()) + ")] ("
                + f(x) + " − " + f(modelo.getX1()) + ")");

        etiquetaPendiente.setText(
                "m = (" + f(modelo.calcularDiferenciaY())
                + ") / (" + f(modelo.calcularDiferenciaX())
                + ") = " + f(modelo.getPendiente()));

        etiquetaSustitucion.setText(
                "y = " + f(modelo.getY1()) + " + ("
                + f(modelo.getPendiente()) + ") × ("
                + f(x) + " − " + f(modelo.getX1()) + ")\n\n"
                + "y = " + f(y));

        etiquetaResultado.setText("y = " + f(y));
        etiquetaValorReal.setText("Valor real: " + f(valorReal));

        etiquetaErrorAbsoluto.setText(
                "Error absoluto: " + f(errorAbsoluto));

        etiquetaErrorRelativo.setText(
                Double.isFinite(errorRelativo)
                        ? "Error relativo: " + f(errorRelativo) + " %"
                        : "Error relativo: no definido (valor real cero)");

        etiquetaCota.setText(
                Double.isFinite(cotaTeorica)
                        ? "Cota teórica del error: " + f(cotaTeorica)
                        : "Cota teórica: no disponible");

        dibujarGrafica(modelo, x, y);
    }

    public void limpiarResultados() {
        etiquetaFormula.setText(
                "y = y₁ + [(y₂ − y₁) / (x₂ − x₁)] (x − x₁)");
        etiquetaPendiente.setText("Pendiente: —");
        etiquetaSustitucion.setText("—");
        etiquetaResultado.setText("—");
        etiquetaValorReal.setText("Valor real: —");
        etiquetaErrorAbsoluto.setText("Error absoluto: —");
        etiquetaErrorRelativo.setText("Error relativo: —");
        etiquetaCota.setText("Cota teórica: —");

        GraphicsContext gc = grafica.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, grafica.getWidth(), grafica.getHeight());
        gc.setFill(Color.web("#64748B"));
        gc.setFont(Font.font("Arial", 14));
        gc.fillText(
                "Ingresa los datos para visualizar la gráfica.",
                190, 195);
    }

    private void dibujarGrafica(
            InterpolacionLineal modelo, double x, double y) {

        GraphicsContext gc = grafica.getGraphicsContext2D();

        double ancho = grafica.getWidth();
        double alto = grafica.getHeight();

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, ancho, alto);

        double x1 = modelo.getX1();
        double x2 = modelo.getX2();
        double y1 = modelo.getY1();
        double y2 = modelo.getY2();

        double minimoX = Math.min(0, Math.min(x1, x2));
        double maximoX = Math.max(0, Math.max(x1, x2));
        double minimoY = Math.min(0, Math.min(y1, y2));
        double maximoY = Math.max(0, Math.max(y1, y2));

        minimoX -= Math.max((maximoX - minimoX) * 0.15, 0.5);
        maximoX += Math.max((maximoX - minimoX) * 0.15, 0.5);
        minimoY -= Math.max((maximoY - minimoY) * 0.18, 0.5);
        maximoY += Math.max((maximoY - minimoY) * 0.18, 0.5);

        final double limiteMinX = minimoX;
        final double limiteMinY = minimoY;
        final double limiteMaxX = maximoX;
        final double limiteMaxY = maximoY;

        final double izquierda = 64;
        final double derecha = ancho - 28;
        final double arriba = 24;
        final double abajo = alto - 48;

        final double escalaHorizontal =
                (derecha - izquierda) / (limiteMaxX - limiteMinX);

        final double escalaVertical =
                (abajo - arriba) / (limiteMaxY - limiteMinY);

        DoubleUnaryOperator px = valor ->
                izquierda
                        + (valor - limiteMinX) * escalaHorizontal;

        DoubleUnaryOperator py = valor ->
                abajo
                        - (valor - limiteMinY) * escalaVertical;

        gc.setFont(Font.font("Arial", 10));

        for (int i = 0; i <= 6; i++) {
            double vx = limiteMinX
                    + (limiteMaxX - limiteMinX) * i / 6.0;

            double vy = limiteMinY
                    + (limiteMaxY - limiteMinY) * i / 6.0;

            gc.setStroke(Color.web("#E9EEF5"));
            gc.setLineWidth(1);

            gc.strokeLine(
                    px.applyAsDouble(vx), arriba,
                    px.applyAsDouble(vx), abajo);

            gc.strokeLine(
                    izquierda, py.applyAsDouble(vy),
                    derecha, py.applyAsDouble(vy));

            gc.setFill(Color.web("#64748B"));
            gc.fillText(
                    f(vx), px.applyAsDouble(vx) - 12, abajo + 18);

            gc.fillText(
                    f(vy), 8, py.applyAsDouble(vy) + 4);
        }

        gc.setStroke(Color.web("#64748B"));
        gc.setLineWidth(1.5);

        if (limiteMinY <= 0 && limiteMaxY >= 0) {
            gc.strokeLine(
                    izquierda, py.applyAsDouble(0),
                    derecha, py.applyAsDouble(0));
        }

        if (limiteMinX <= 0 && limiteMaxX >= 0) {
            gc.strokeLine(
                    px.applyAsDouble(0), arriba,
                    px.applyAsDouble(0), abajo);
        }

        gc.setLineDashes(5, 5);
        gc.setStroke(Color.web("#94A3B8"));
        gc.setLineWidth(1.3);

        gc.strokeLine(
                px.applyAsDouble(x1), py.applyAsDouble(y1),
                px.applyAsDouble(x1), abajo);

        gc.strokeLine(
                px.applyAsDouble(x2), py.applyAsDouble(y2),
                px.applyAsDouble(x2), abajo);

        gc.setStroke(Color.web("#16A34A"));

        gc.strokeLine(
                px.applyAsDouble(x), py.applyAsDouble(y),
                px.applyAsDouble(x), abajo);

        gc.strokeLine(
                izquierda, py.applyAsDouble(y),
                px.applyAsDouble(x), py.applyAsDouble(y));

        gc.setLineDashes();

        gc.setStroke(Color.web("#2563EB"));
        gc.setLineWidth(3);

        gc.strokeLine(
                px.applyAsDouble(x1), py.applyAsDouble(y1),
                px.applyAsDouble(x2), py.applyAsDouble(y2));

        gc.setFill(Color.web("#2563EB"));

        gc.fillOval(
                px.applyAsDouble(x1) - 5,
                py.applyAsDouble(y1) - 5,
                10, 10);

        gc.fillOval(
                px.applyAsDouble(x2) - 5,
                py.applyAsDouble(y2) - 5,
                10, 10);

        gc.setFill(Color.web("#16A34A"));

        gc.fillOval(
                px.applyAsDouble(x) - 6,
                py.applyAsDouble(y) - 6,
                12, 12);

        gc.setFont(Font.font("Arial", 11));
        gc.setFill(Color.web("#1D4ED8"));

        gc.fillText(
                "P1 (" + f(x1) + ", " + f(y1) + ")",
                Math.min(px.applyAsDouble(x1) + 8, ancho - 150),
                Math.max(16, py.applyAsDouble(y1) - 10));

        gc.fillText(
                "P2 (" + f(x2) + ", " + f(y2) + ")",
                Math.min(px.applyAsDouble(x2) + 8, ancho - 150),
                Math.max(16, py.applyAsDouble(y2) - 10));

        gc.setFill(Color.web("#15803D"));

        gc.fillText(
                "P (" + f(x) + ", " + f(y) + ")",
                Math.min(px.applyAsDouble(x) + 8, ancho - 150),
                Math.max(16, py.applyAsDouble(y) - 10));

        gc.setFill(Color.web("#334155"));
        gc.setFont(Font.font("Arial", 12));

        gc.fillText("x", derecha + 8, abajo + 5);
        gc.fillText("y", izquierda - 5, arriba - 9);
    }

    private String f(double valor) {
        if (!Double.isFinite(valor)) {
            return "No definido";
        }

        if (Math.abs(valor) < 1e-12) {
            valor = 0;
        }

        return BigDecimal.valueOf(valor)
                .setScale(6, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString();
    }

    public void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Datos no válidos");
        alerta.setHeaderText("Revisa los valores ingresados");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    public BorderPane getRoot() {
        return root;
    }

    public TextField getCampoX1() {
        return campoX1;
    }

    public TextField getCampoY1() {
        return campoY1;
    }

    public TextField getCampoX2() {
        return campoX2;
    }

    public TextField getCampoY2() {
        return campoY2;
    }

    public TextField getCampoX() {
        return campoX;
    }

    public TextField getCampoFuncion() {
        return campoFuncion;
    }

    public Button getBotonCalcular() {
        return botonCalcular;
    }

    public Button getBotonLimpiar() {
        return botonLimpiar;
    }

    public void restablecerCampos() {
        campoX1.setText("2");
        campoY1.setText("ln(2)");
        campoX2.setText("4");
        campoY2.setText("ln(4)");
        campoX.setText("3");
        campoFuncion.setText("ln(x)");

        limpiarResultados();
    }
}
