
package com.interpolacionlineal.controlador;

import com.interpolacionlineal.modelo.CalculoError;
import com.interpolacionlineal.modelo.InterpolacionLineal;
import com.interpolacionlineal.servicio.EvaluadorFuncion;
import com.interpolacionlineal.vista.VistaPrincipal;

public class ControladorInterpolacion {

    private final VistaPrincipal vista;
    private final EvaluadorFuncion evaluador;

    public ControladorInterpolacion(VistaPrincipal vista) {
        this.vista = vista;
        this.evaluador = new EvaluadorFuncion();
    }

    public void inicializar() {
        vista.getBotonCalcular().setOnAction(
                evento -> calcular());

        vista.getBotonLimpiar().setOnAction(evento -> {
            vista.restablecerCampos();
            vista.limpiarResultados();
        });

        vista.limpiarResultados();
    }

    private void calcular() {
        try {
            double x1 = evaluador.interpretarNumero(
                    vista.getCampoX1().getText());

            double y1 = evaluador.interpretarNumero(
                    vista.getCampoY1().getText());

            double x2 = evaluador.interpretarNumero(
                    vista.getCampoX2().getText());

            double y2 = evaluador.interpretarNumero(
                    vista.getCampoY2().getText());

            double x = evaluador.interpretarNumero(
                    vista.getCampoX().getText());

            InterpolacionLineal modelo =
                    new InterpolacionLineal(x1, y1, x2, y2);

            double resultado = modelo.interpolar(x);

            double valorReal = evaluador.evaluarFuncion(
                    vista.getCampoFuncion().getText(), x);

            CalculoError error = new CalculoError(
                    valorReal,
                    resultado,
                    x,
                    x1,
                    x2);

            vista.mostrarResultados(
                    modelo,
                    x,
                    resultado,
                    error.getValorReal(),
                    error.getErrorAbsoluto(),
                    error.getErrorRelativo(),
                    error.getCotaTeorica());

        } catch (NumberFormatException e) {
            vista.mostrarError(
                    "Introduce números válidos o expresiones como ln(4).");
        } catch (IllegalArgumentException e) {
            vista.mostrarError(e.getMessage());
        } catch (ArithmeticException e) {
            vista.mostrarError(
                    "No se pudo completar el cálculo matemático.");
        }
    }
}
