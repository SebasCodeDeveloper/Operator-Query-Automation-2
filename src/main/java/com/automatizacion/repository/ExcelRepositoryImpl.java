package com.automatizacion.repository;

import com.automatizacion.model.ExcelManager;

import java.util.List;

/**
 * Implementación de la interfaz ExcelRepository. * Actúa como un adaptador que delega las operaciones de bajo nivel * a la utilidad especializada ExcelManager. * * @author SebasCodeDev * @version 1.3.1 * @since 01/24/2026
 */
public class ExcelRepositoryImpl implements ExcelRepository {

    /**
     * Recupera la lista de contactos delegando la lectura al gestor de Excel.
     */
    @Override
    public List<String> leerContactos(String rutaExcel) {
        return ExcelManager.leerContactos(rutaExcel);
    }

    /**
     * Registra el resultado del operador invocando el método de escritura del gestor.
     */
    @Override
    public void escribirOperador(String rutaExcel, int fila, String operador) {
        ExcelManager.escribirOperador(rutaExcel, fila, operador);
    }

    /**
     * Obtiene los índices de filas fallidas consultando la lógica del gestor de Excel.
     */
    @Override
    public List<Integer> obtenerFilasNoDetectados(String rutaExcel) {
        return ExcelManager.obtenerFilasNoDetectados(rutaExcel);
    }
}