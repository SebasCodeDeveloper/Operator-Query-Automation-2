package com.automatizacion.repository;

import com.automatizacion.model.ExcelManager;
import java.util.List;

public class ExcelRepositoryImpl implements ExcelRepository {

    @Override
    public List<String> leerContactos(String rutaExcel) {
        return ExcelManager.leerContactos(rutaExcel);
    }

    @Override
    public void escribirOperador(String rutaExcel, int fila, String operador) {
        ExcelManager.escribirOperador(rutaExcel, fila, operador);
    }

    @Override
    public List<Integer> obtenerFilasNoDetectados(String rutaExcel) {
        return ExcelManager.obtenerFilasNoDetectados(rutaExcel);
    }
}
