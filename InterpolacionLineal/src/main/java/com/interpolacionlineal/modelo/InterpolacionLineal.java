
package com.interpolacionlineal.modelo;

public class InterpolacionLineal {

    private final double x1;
    private final double y1;
    private final double x2;
    private final double y2;
    private final double pendiente;

    public InterpolacionLineal(
            double x1, double y1,
            double x2, double y2) {

        if (!Double.isFinite(x1) || !Double.isFinite(y1)
                || !Double.isFinite(x2) || !Double.isFinite(y2)) {
            throw new IllegalArgumentException(
                    "Todos los valores deben ser finitos.");
        }

        if (x1 == x2) {
            throw new IllegalArgumentException(
                    "x1 y x2 deben ser diferentes.");
        }

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

        this.pendiente = (y2 - y1) / (x2 - x1);
    }

    public double interpolar(double x) {
        if (!Double.isFinite(x)) {
            throw new IllegalArgumentException(
                    "El valor de x debe ser finito.");
        }

        if (x < Math.min(x1, x2) || x > Math.max(x1, x2)) {
            throw new IllegalArgumentException(
                    "El valor de x debe estar entre x1 y x2.");
        }

        return y1 + pendiente * (x - x1);
    }

    public double getPendiente() {
        return pendiente;
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }

    public double calcularDiferenciaY() {
        return y2 - y1;
    }

    public double calcularDiferenciaX() {
        return x2 - x1;
    }
}
