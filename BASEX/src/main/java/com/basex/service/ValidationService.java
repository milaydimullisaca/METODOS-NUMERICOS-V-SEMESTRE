package com.basex.service;

public class ValidationService {

    private static final String DIGITOS =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public boolean validarNumero(
            String numero,
            int base) {

        return validarNumero(
                numero,
                base,
                false
        );
    }

    public boolean validarNumero(
            String numero,
            int base,
            boolean modoFraccionario) {

        if (numero == null || numero.isBlank()) {
            return false;
        }

        numero = numero.toUpperCase().trim();

        if (modoFraccionario) {

            if (numero.length() == 0) {
                return false;
            }

            int cantidadPuntos = 0;

            for (char caracter : numero.toCharArray()) {

                if (caracter == '.') {
                    cantidadPuntos++;
                }
            }

            if (cantidadPuntos != 1) {
                return false;
            }

            String[] partes =
                    numero.split("\\.", -1);

            String parteEntera = partes[0];
            String parteFraccionaria = partes[1];

            if (parteEntera.isEmpty()
                    || parteFraccionaria.isEmpty()) {

                return false;
            }

            return validarDigitos(
                    numero.replace(".", ""),
                    base
            );
        }

        if (numero.contains(".")) {
            return false;
        }

        return validarDigitos(
                numero,
                base
        );
    }

    private boolean validarDigitos(
            String numero,
            int base) {

        for (char caracter :
                numero.toCharArray()) {

            int valor =
                    DIGITOS.indexOf(caracter);

            if (valor < 0 || valor >= base) {
                return false;
            }
        }

        return true;
    }

    public String obtenerMensajeError(
            String numero,
            int base) {

        return obtenerMensajeError(
                numero,
                base,
                false
        );
    }

    public String obtenerMensajeError(
            String numero,
            int base,
            boolean modoFraccionario) {

        if (numero == null || numero.isBlank()) {

            return "Ingrese un número.";
        }

        numero =
                numero.toUpperCase().trim();

        if (modoFraccionario) {

            int cantidadPuntos = 0;

            for (char caracter :
                    numero.toCharArray()) {

                if (caracter == '.') {
                    cantidadPuntos++;
                }
            }

            if (cantidadPuntos == 0) {

                return "Ingrese un número fraccionario "
                        + "utilizando un punto decimal.\n\n"
                        + "Ejemplo: 101.101";
            }

            if (cantidadPuntos > 1) {

                return "El número no puede contener "
                        + "más de un punto decimal.";
            }

            String[] partes =
                    numero.split("\\.", -1);

            if (partes[0].isEmpty()) {

                return "Debe ingresar la parte entera "
                        + "del número.\n\n"
                        + "Ejemplo: 5.625";
            }

            if (partes[1].isEmpty()) {

                return "Debe ingresar la parte fraccionaria "
                        + "del número.\n\n"
                        + "Ejemplo: 5.625";
            }

            String numeroSinPunto =
                    numero.replace(".", "");

            return obtenerMensajeDigitos(
                    numeroSinPunto,
                    base
            );
        }

        if (numero.contains(".")) {

            return "Está utilizando un número fraccionario, "
                    + "pero el modo seleccionado es Enteros.\n\n"
                    + "Seleccione el modo Fraccionarios.";
        }

        return obtenerMensajeDigitos(
                numero,
                base
        );
    }

    private String obtenerMensajeDigitos(
            String numero,
            int base) {

        for (char caracter :
                numero.toCharArray()) {

            int valor =
                    DIGITOS.indexOf(caracter);

            if (valor < 0) {

                return "El carácter '"
                        + caracter
                        + "' no es válido.";
            }

            if (valor >= base) {

                return "El carácter '"
                        + caracter
                        + "' no pertenece "
                        + "a la base "
                        + base
                        + ".";
            }
        }

        return "";
    }
}