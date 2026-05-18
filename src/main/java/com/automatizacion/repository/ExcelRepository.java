package com.automatizacion.repository;

import java.util.List;

/**
 * Interfaz que define el contrato para la gestión de datos en archivos Excel. * Proporciona los métodos necesarios para la lectura de entradas y persistencia de resultados. * * @author SebasCodeDev * @version 1.3.1 * @since 01/24/2026
 */
public interface ExcelRepository {

    /**
     * Extrae la lista de contactos (números) desde el archivo Excel especificado. * @param rutaExcel Ruta del archivo de origen. * @return Lista de strings con los contactos procesados.
     */
    List<String> leerContactos(String rutaExcel);

    /**
     * Registra el nombre del operador identificado en la fila correspondiente. * @param rutaExcel Ruta del archivo donde se guardará la información. * @param fila Índice de la fila a actualizar. * @param operador Nombre del operador obtenido en la consulta.
     */
    void escribirOperador(String rutaExcel, int fila, String operador);

    /**
     * Identifica y retorna los índices de las filas que no fueron procesadas con éxito. * @param rutaExcel Ruta del archivo a analizar. * @return Lista de enteros que representan las filas para reprocesamiento.
     */
    List<Integer> obtenerFilasNoDetectados(String rutaExcel);
}