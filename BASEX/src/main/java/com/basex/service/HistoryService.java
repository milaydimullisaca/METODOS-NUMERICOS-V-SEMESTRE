package com.basex.service;

import com.basex.model.Conversion;
import java.util.ArrayList;
import java.util.List;

public class HistoryService {

    private final List<Conversion> historial = new ArrayList<>();

    public void agregar(Conversion conversion) {
        historial.add(conversion);
    }

    public List<Conversion> obtenerHistorial() {
        return new ArrayList<>(historial);
    }

    public void limpiar() {
        historial.clear();
    }

    public int cantidad() {
        return historial.size();
    }
}