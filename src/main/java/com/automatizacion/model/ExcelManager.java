package com.automatizacion.model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestor encargado de la persistencia de datos en archivos Excel.
 * Maneja la lectura de contactos y la actualización de estados de los operadores.
 *
 * @author SebasCodeDev
 * @version 1.3.1
 * @since 01/24/2026
 */
public class ExcelManager {

    /**
     * Extrae la lista de números telefónicos desde la primera columna de la hoja principal.
     * @param rutaExcel Ruta absoluta o relativa del archivo .xlsx
     * @return Lista de strings con los números encontrados.
     */
    public static List<String> leerContactos(String rutaExcel) {
        List<String> contactos = new ArrayList<>();
        try (FileInputStream file = new FileInputStream(rutaExcel);
             Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheet("Hoja1");

            if (sheet == null) {
                System.err.println("⚠ No se encontró la hoja 'Hoja1'. Verifica el nombre.");
                return contactos;
            }

            // Recorre desde la segunda fila para omitir los encabezados
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row fila = sheet.getRow(i);
                if (fila == null) continue;

                Cell celda = fila.getCell(0);
                if (celda == null) continue;

                // Normalización de datos para evitar notación científica en números largos
                String numero = "";
                if (celda.getCellType() == CellType.NUMERIC) {
                    numero = String.valueOf((long) celda.getNumericCellValue());
                } else {
                    numero = celda.getStringCellValue().trim();
                }

                if (!numero.isEmpty()) {
                    contactos.add(numero);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return contactos;
    }

    /**
     * Registra el resultado del operador en la columna correspondiente del archivo Excel.
     * @param rutaExcel Ruta del archivo.
     * @param fila      Índice de la fila a modificar.
     * @param operador  Texto con el nombre del operador detectado.
     */
    public static void escribirOperador(String rutaExcel, int fila, String operador) {
        try (FileInputStream file = new FileInputStream(rutaExcel);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheet("Hoja1");
            Row row = sheet.getRow(fila);

            // Crea o sobrescribe la celda en la columna de resultados (Columna B)
            Cell cell = row.createCell(1);
            cell.setCellValue(operador);

            // Flujo de salida para guardar los cambios en el disco
            try (FileOutputStream out = new FileOutputStream(rutaExcel)) {
                workbook.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Filtra las filas que no pudieron ser procesadas correctamente para su reintento.
     * @param rutaExcel Ruta del archivo.
     * @return Lista de índices de filas marcadas con error o no detectadas.
     */
    public static List<Integer> obtenerFilasNoDetectados(String rutaExcel) {
        List<Integer> filasReprocesar = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(rutaExcel);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheet("Hoja1");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row fila = sheet.getRow(i);
                if (fila == null) continue;

                // Evaluación del estado en la columna de resultados
                Cell celdaOperador = fila.getCell(1);

                if (celdaOperador != null) {
                    String valor = celdaOperador.getStringCellValue().trim().toUpperCase();

                    // Identificación de estados que requieren una nueva consulta
                    if (valor.equals("NO DETECTADO") || valor.equals("NO ENCONTRADO")) {
                        filasReprocesar.add(i);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return filasReprocesar;
    }
}