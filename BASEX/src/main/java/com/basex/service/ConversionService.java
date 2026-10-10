package com.basex.service;

public class ConversionService {

    private static final String DIGITOS =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int MAX_DECIMALES = 12;

    public String convertir(
            String numero,
            int baseOrigen,
            int baseDestino) {

        numero = numero.toUpperCase().trim();

        if (baseOrigen == baseDestino) {
            return numero;
        }

        if (numero.contains(".")) {
            return convertirFraccionario(
                    numero,
                    baseOrigen,
                    baseDestino
            );
        }

        long decimal =
                convertirADecimal(
                        numero,
                        baseOrigen
                );

        return convertirDesdeDecimal(
                decimal,
                baseDestino
        );
    }

    public long convertirADecimal(
            String numero,
            int base) {

        numero = numero.toUpperCase();

        long decimal = 0;

        for (int i = 0; i < numero.length(); i++) {

            char caracter = numero.charAt(i);

            int valor =
                    DIGITOS.indexOf(caracter);

            decimal =
                    decimal * base + valor;
        }

        return decimal;
    }

    public String convertirDesdeDecimal(
            long decimal,
            int base) {

        if (decimal == 0) {
            return "0";
        }

        boolean negativo = decimal < 0;

        if (negativo) {
            decimal = Math.abs(decimal);
        }

        StringBuilder resultado =
                new StringBuilder();

        while (decimal > 0) {

            int residuo =
                    (int) (decimal % base);

            resultado.insert(
                    0,
                    DIGITOS.charAt(residuo)
            );

            decimal =
                    decimal / base;
        }

        if (negativo) {
            resultado.insert(0, "-");
        }

        return resultado.toString();
    }

    private String convertirFraccionario(
            String numero,
            int baseOrigen,
            int baseDestino) {

        String[] partes = numero.split("\\.", -1);

        String parteEntera = partes[0];
        String parteFraccionaria = partes[1];

        double decimal =
                convertirFraccionADecimal(
                        parteEntera,
                        parteFraccionaria,
                        baseOrigen
                );

        return convertirDecimalAFraccion(
                decimal,
                baseDestino
        );
    }

    private double convertirFraccionADecimal(
            String parteEntera,
            String parteFraccionaria,
            int base) {

        double resultado = 0;

        if (!parteEntera.isEmpty()) {

            resultado =
                    convertirADecimal(
                            parteEntera,
                            base
                    );
        }

        for (int i = 0;
             i < parteFraccionaria.length();
             i++) {

            char caracter =
                    parteFraccionaria.charAt(i);

            int valor =
                    DIGITOS.indexOf(caracter);

            resultado +=
                    valor /
                    Math.pow(
                            base,
                            i + 1
                    );
        }

        return resultado;
    }

    private String convertirDecimalAFraccion(
            double decimal,
            int base) {

        long parteEntera =
                (long) decimal;

        double parteFraccionaria =
                decimal - parteEntera;

        String resultadoEntero =
                convertirDesdeDecimal(
                        parteEntera,
                        base
                );

        if (parteFraccionaria < 0.000000000001) {
            return resultadoEntero;
        }

        StringBuilder resultado =
                new StringBuilder();

        resultado.append(resultadoEntero);
        resultado.append(".");

        int contador = 0;

        while (
                parteFraccionaria > 0.000000000001
                && contador < MAX_DECIMALES
        ) {

            parteFraccionaria *= base;

            int digito =
                    (int) parteFraccionaria;

            resultado.append(
                    DIGITOS.charAt(digito)
            );

            parteFraccionaria -= digito;

            contador++;
        }

        return resultado.toString();
    }

    public String convertirATodas(
            String numero,
            int baseOrigen) {

        StringBuilder resultado =
                new StringBuilder();

        int[] bases = {
                2, 8, 10, 16
        };

        String[] nombres = {
                "BINARIO",
                "OCTAL",
                "DECIMAL",
                "HEXADECIMAL"
        };

        for (int i = 0; i < bases.length; i++) {

            String valor =
                    convertir(
                            numero,
                            baseOrigen,
                            bases[i]
                    );

            resultado
                    .append(nombres[i])
                    .append(" (Base ")
                    .append(bases[i])
                    .append("): ")
                    .append(valor)
                    .append("\n");
        }

        return resultado.toString();
    }

    public String obtenerProcedimiento(
            String numero,
            int baseOrigen,
            int baseDestino) {

        numero = numero.toUpperCase();

        if (numero.contains(".")) {

            return obtenerProcedimientoFraccionario(
                    numero,
                    baseOrigen,
                    baseDestino
            );
        }

        return obtenerProcedimientoEntero(
                numero,
                baseOrigen,
                baseDestino
        );
    }

    private String obtenerProcedimientoEntero(
            String numero,
            int baseOrigen,
            int baseDestino) {

        StringBuilder procedimiento =
                new StringBuilder();

        procedimiento.append(
                "PROCEDIMIENTO DE CONVERSIÓN\n\n"
        );

        procedimiento.append(
                "1. NÚMERO ORIGINAL\n\n"
        );

        procedimiento.append("   ")
                .append(numero)
                .append(" (Base ")
                .append(baseOrigen)
                .append(")\n\n");

        if (baseOrigen == baseDestino) {

            procedimiento.append(
                    "2. LAS BASES SON IGUALES\n\n"
            );

            procedimiento.append(
                    "   No es necesario realizar "
                    + "ninguna conversión.\n\n"
            );

            procedimiento.append(
                    "3. RESULTADO FINAL\n\n"
            );

            procedimiento.append("   ")
                    .append(numero)
                    .append(" (Base ")
                    .append(baseDestino)
                    .append(")");

            return procedimiento.toString();
        }

        long decimal =
                convertirADecimal(
                        numero,
                        baseOrigen
                );

        procedimiento.append(
                "2. CONVERSIÓN A DECIMAL\n\n"
        );

        procedimiento.append(
                obtenerProcedimientoADecimal(
                        numero,
                        baseOrigen
                )
        );

        if (baseDestino == 10) {

            procedimiento.append("\n\n");
            procedimiento.append(
                    "3. RESULTADO FINAL\n\n"
            );

            procedimiento.append("   ")
                    .append(numero)
                    .append(" (Base ")
                    .append(baseOrigen)
                    .append(") = ")
                    .append(decimal)
                    .append(" (Base 10)");

            return procedimiento.toString();
        }

        procedimiento.append("\n\n");

        procedimiento.append(
                "3. CONVERSIÓN DE DECIMAL A BASE "
        );

        procedimiento.append(baseDestino)
                .append("\n\n");

        procedimiento.append(
                obtenerDivisionesSucesivas(
                        decimal,
                        baseDestino
                )
        );

        String resultado =
                convertirDesdeDecimal(
                        decimal,
                        baseDestino
                );

        procedimiento.append("\n\n");

        procedimiento.append(
                "4. RESULTADO FINAL\n\n"
        );

        procedimiento.append("   ")
                .append(numero)
                .append(" (Base ")
                .append(baseOrigen)
                .append(") = ")
                .append(resultado)
                .append(" (Base ")
                .append(baseDestino)
                .append(")");

        return procedimiento.toString();
    }

    private String obtenerProcedimientoFraccionario(
            String numero,
            int baseOrigen,
            int baseDestino) {

        StringBuilder procedimiento =
                new StringBuilder();

        String[] partes =
                numero.split("\\.", -1);

        String parteEntera = partes[0];
        String parteFraccionaria = partes[1];

        procedimiento.append(
                "PROCEDIMIENTO DE CONVERSIÓN FRACCIONARIA\n\n"
        );

        procedimiento.append(
                "1. NÚMERO ORIGINAL\n\n"
        );

        procedimiento.append("   ")
                .append(numero)
                .append(" (Base ")
                .append(baseOrigen)
                .append(")\n\n");

        double decimal =
                convertirFraccionADecimal(
                        parteEntera,
                        parteFraccionaria,
                        baseOrigen
                );

        procedimiento.append(
                "2. CONVERSIÓN A DECIMAL\n\n"
        );

        if (!parteEntera.isEmpty()) {

            procedimiento.append(
                    "   PARTE ENTERA:\n\n"
            );

            procedimiento.append(
                    obtenerProcedimientoADecimal(
                            parteEntera,
                            baseOrigen
                    )
            );

            procedimiento.append("\n\n");
        }

        procedimiento.append(
                "   PARTE FRACCIONARIA:\n\n"
        );

        double sumaFraccionaria = 0;

        for (int i = 0;
             i < parteFraccionaria.length();
             i++) {

            char caracter =
                    parteFraccionaria.charAt(i);

            int valor =
                    DIGITOS.indexOf(caracter);

            double potencia =
                    Math.pow(
                            baseOrigen,
                            -(i + 1)
                    );

            double resultado =
                    valor * potencia;

            sumaFraccionaria += resultado;

            procedimiento.append("   ")
                    .append(caracter)
                    .append(" × ")
                    .append(baseOrigen)
                    .append("^(-")
                    .append(i + 1)
                    .append(") = ")
                    .append(resultado)
                    .append("\n");
        }

        procedimiento.append("\n");

        procedimiento.append(
                "   Suma fraccionaria = "
        );

        procedimiento.append(
                sumaFraccionaria
        );

        procedimiento.append("\n\n");

        procedimiento.append(
                "   Valor decimal total = "
        );

        procedimiento.append(decimal);

        if (baseDestino == 10) {

            procedimiento.append("\n\n");

            procedimiento.append(
                    "3. RESULTADO FINAL\n\n"
            );

            procedimiento.append("   ")
                    .append(numero)
                    .append(" (Base ")
                    .append(baseOrigen)
                    .append(") = ")
                    .append(decimal)
                    .append(" (Base 10)");

            return procedimiento.toString();
        }

        procedimiento.append("\n\n");

        procedimiento.append(
                "3. CONVERSIÓN DE DECIMAL A BASE "
        );

        procedimiento.append(baseDestino)
                .append("\n\n");

        procedimiento.append(
                obtenerProcedimientoFraccionarioDesdeDecimal(
                        decimal,
                        baseDestino
                )
        );

        String resultado =
                convertirDecimalAFraccion(
                        decimal,
                        baseDestino
                );

        procedimiento.append("\n\n");

        procedimiento.append(
                "4. RESULTADO FINAL\n\n"
        );

        procedimiento.append("   ")
                .append(numero)
                .append(" (Base ")
                .append(baseOrigen)
                .append(") = ")
                .append(resultado)
                .append(" (Base ")
                .append(baseDestino)
                .append(")");

        return procedimiento.toString();
    }

    private String obtenerProcedimientoFraccionarioDesdeDecimal(
            double decimal,
            int base) {

        StringBuilder texto =
                new StringBuilder();

        long parteEntera =
                (long) decimal;

        double fraccion =
                decimal - parteEntera;

        texto.append(
                "   PARTE ENTERA:\n\n"
        );

        texto.append(
                obtenerDivisionesSucesivas(
                        parteEntera,
                        base
                )
        );

        texto.append("\n\n");

        texto.append(
                "   PARTE FRACCIONARIA:\n\n"
        );

        int contador = 0;

        while (
                fraccion > 0.000000000001
                && contador < MAX_DECIMALES
        ) {

            double multiplicacion =
                    fraccion * base;

            int digito =
                    (int) multiplicacion;

            double nuevoValor =
                    multiplicacion - digito;

            texto.append("   ")
                    .append(fraccion)
                    .append(" × ")
                    .append(base)
                    .append(" = ")
                    .append(multiplicacion)
                    .append(" → ")
                    .append(DIGITOS.charAt(digito))
                    .append("\n");

            fraccion = nuevoValor;
            contador++;
        }

        texto.append("\n");

        texto.append(
                "   Los dígitos fraccionarios se "
                + "leen en el orden obtenido."
        );

        return texto.toString();
    }

    private String obtenerProcedimientoADecimal(
            String numero,
            int base) {

        StringBuilder texto =
                new StringBuilder();

        long suma = 0;

        for (int i = 0;
             i < numero.length();
             i++) {

            char caracter =
                    numero.charAt(i);

            int valor =
                    DIGITOS.indexOf(caracter);

            int potencia =
                    numero.length() - 1 - i;

            long resultado =
                    (long) valor *
                    potenciaBase(
                            base,
                            potencia
                    );

            suma += resultado;

            texto.append("   ")
                    .append(caracter)
                    .append(" × ")
                    .append(base)
                    .append("^")
                    .append(potencia)
                    .append(" = ")
                    .append(resultado)
                    .append("\n");
        }

        texto.append("\n");

        texto.append("   Suma:\n");

        for (int i = 0;
             i < numero.length();
             i++) {

            char caracter =
                    numero.charAt(i);

            int valor =
                    DIGITOS.indexOf(caracter);

            int potencia =
                    numero.length() - 1 - i;

            long resultado =
                    (long) valor *
                    potenciaBase(
                            base,
                            potencia
                    );

            texto.append(resultado);

            if (i < numero.length() - 1) {
                texto.append(" + ");
            }
        }

        texto.append(" = ")
                .append(suma);

        return texto.toString();
    }

    private long potenciaBase(
            int base,
            int exponente) {

        long resultado = 1;

        for (int i = 0;
             i < exponente;
             i++) {

            resultado *= base;
        }

        return resultado;
    }

    private String obtenerDivisionesSucesivas(
            long decimal,
            int base) {

        if (decimal == 0) {

            return "   0 ÷ "
                    + base
                    + " = 0   residuo 0";
        }

        StringBuilder texto =
                new StringBuilder();

        long numeroActual = decimal;

        texto.append(
                "   Divisiones sucesivas:\n\n"
        );

        while (numeroActual > 0) {

            long cociente =
                    numeroActual / base;

            int residuo =
                    (int) (numeroActual % base);

            texto.append("   ")
                    .append(numeroActual)
                    .append(" ÷ ")
                    .append(base)
                    .append(" = ")
                    .append(cociente)
                    .append("   residuo ")
                    .append(residuo);

            if (residuo >= 10) {

                texto.append(" (")
                        .append(
                                DIGITOS.charAt(residuo)
                        )
                        .append(")");
            }

            texto.append("\n");

            numeroActual = cociente;
        }

        texto.append(
                "\n   Los residuos se leen "
                + "de abajo hacia arriba."
        );

        return texto.toString();
    }
}