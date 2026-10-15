
package com.interpolacionlineal.modelo;

public class CalculoError {

    private final double valorReal;
    private final double valorInterpolado;
    private final double errorAbsoluto;
    private final double errorRelativo;
    private final double cotaTeorica;

    public CalculoError(
            double valorReal,
            double valorInterpolado,
            double x,
            double x1,
            double x2) {

        if (!Double.isFinite(valorReal)
                || !Double.isFinite(valorInterpolado)) {
            throw new IllegalArgumentException(
                    "Los valores deben ser finitos.");
        }

        this.valorReal = valorReal;
        this.valorInterpolado = valorInterpolado;

        this.errorAbsoluto =
                Math.abs(valorReal - valorInterpolado);

        this.errorRelativo = Math.abs(valorReal) > 1e-12
                ? errorAbsoluto / Math.abs(valorReal) * 100.0
                : Double.NaN;

        if (x1 <= 0 || x2 <= 0) {
            this.cotaTeorica = Double.NaN;
        } else {
            double minimo = Math.min(x1, x2);
            double maximoSegundaDerivada = 1.0 / (minimo * minimo);

            this.cotaTeorica = maximoSegundaDerivada / 2.0
                    * Math.abs((x - x1) * (x - x2));
        }
    }

    public double getValorReal() {
        return valorReal;
    }

    public double getValorInterpolado() {
        return valorInterpolado;
    }

    public double getErrorAbsoluto() {
        return errorAbsoluto;
    }

    public double getErrorRelativo() {
        return errorRelativo;
    }

    public double getCotaTeorica() {
        return cotaTeorica;
    }
}
