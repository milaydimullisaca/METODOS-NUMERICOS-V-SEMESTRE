package com.basex.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Conversion {

    private final String numeroOriginal;
    private final int baseOrigen;
    private final int baseDestino;
    private final String resultado;
    private final LocalDateTime fecha;

    public Conversion(
            String numeroOriginal,
            int baseOrigen,
            int baseDestino,
            String resultado) {

        this.numeroOriginal = numeroOriginal;
        this.baseOrigen = baseOrigen;
        this.baseDestino = baseDestino;
        this.resultado = resultado;
        this.fecha = LocalDateTime.now();
    }

    public String getNumeroOriginal() {
        return numeroOriginal;
    }

    public int getBaseOrigen() {
        return baseOrigen;
    }

    public int getBaseDestino() {
        return baseDestino;
    }

    public String getResultado() {
        return resultado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getFechaFormateada() {
        return fecha.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        );
    }
}