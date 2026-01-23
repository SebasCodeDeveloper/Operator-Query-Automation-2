package com.automatizacion.repository;

import java.util.List;

public interface ExcelRepository {

    List<String> leerContactos(String rutaExcel);

    void escribirOperador(String rutaExcel, int fila, String operador);

    List<Integer> obtenerFilasNoDetectados(String rutaExcel);
}
