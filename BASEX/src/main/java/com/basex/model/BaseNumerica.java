package com.basex.model;

public class BaseNumerica {

    private final int base;
    private final String nombre;

    public BaseNumerica(int base, String nombre) {
        this.base = base;
        this.nombre = nombre;
    }

    public int getBase() {
        return base;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return base + " - " + nombre;
    }
}