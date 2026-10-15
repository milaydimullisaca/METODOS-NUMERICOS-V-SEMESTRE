
package com.interpolacionlineal.servicio;

import java.util.Locale;

public class EvaluadorFuncion {

    public double interpretarNumero(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "No dejes campos vacíos.");
        }

        String expresion = texto.trim()
                .toLowerCase(Locale.ROOT)
                .replace(" ", "");

        double resultado;

        if (expresion.startsWith("ln")) {
            String argumento = expresion.substring(2);

            if (argumento.startsWith("(")
                    && argumento.endsWith(")")) {
                argumento = argumento.substring(
                        1, argumento.length() - 1);
            }

            if (argumento.isEmpty()) {
                throw new IllegalArgumentException(
                        "Escribe el argumento del logaritmo.");
            }

            double valor = Double.parseDouble(argumento);

            if (valor <= 0 || !Double.isFinite(valor)) {
                throw new IllegalArgumentException(
                        "El argumento de ln debe ser positivo.");
            }

            resultado = Math.log(valor);
        } else {
            resultado = Double.parseDouble(expresion);
        }

        if (!Double.isFinite(resultado)) {
            throw new IllegalArgumentException(
                    "El resultado debe ser un número finito.");
        }

        return resultado;
    }

    public double evaluarFuncion(String funcion, double x) {
        if (funcion == null || !Double.isFinite(x)) {
            throw new IllegalArgumentException(
                    "La función o el valor de x no son válidos.");
        }

        String expresion = funcion.trim()
                .toLowerCase(Locale.ROOT)
                .replace(" ", "");

        if (expresion.equals("ln(x)")
                || expresion.equals("lnx")) {

            if (x <= 0) {
                throw new IllegalArgumentException(
                        "ln(x) requiere que x sea positivo.");
            }

            return Math.log(x);
        }

        throw new IllegalArgumentException(
                "Función no admitida. Utiliza ln(x).");
    }
}
