package com.automatizacion;

import com.automatizacion.Controller.BusquedaContactos;
import com.automatizacion.Model.ConfigManager;
import com.automatizacion.Model.ExcelManager;
import com.automatizacion.View.ConsolaView;

import javax.swing.*;
import java.util.Scanner;

/**
 * Punto de entrada principal de la aplicación Consulta de Operadores.
 * <p>
 * La clase {@code App} inicializa los componentes esenciales del sistema,
 * muestra un menú interactivo en consola y permite ejecutar dos procesos:
 * <ul>
 *     <li><b>Ejecutar desde una fila específica:</b> solicitan la fila mediante una
 *     ventana emergente (JOptionPane) y el controlador inicia la búsqueda desde allí.</li>
 *     <li><b>Reprocesar NO DETECTADOS:</b> ejecuta directamente sin solicitar información adicional.</li>
 * </ul>
 * <p>
 * Funcionalidades principales:
 * <ul>
 *     <li>Obtiene la ruta del archivo Excel desde el archivo {@code config.properties}.</li>
 *     <li>Inicializa el modelo ({@link ExcelManager}), la vista ({@link ConsolaView})
 *     y el controlador ({@link BusquedaContactos}).</li>
 *     <li>Presenta un menú de selección para ejecutar los diferentes procesos.</li>
 * </ul>
 * <p>
 * Ejemplo de ejecución:
 * <pre>
 *     java -jar ConsultaOperador.jar
 * </pre>
 *
 * @author
 *     Jhoan Sebastián Peña Ordoñez
 * @version
 *     2.1
 * @since
 *     06/11/2025
 */
public class App {
    /**
     * Método principal (entry point) de la aplicación.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {

        // Obtiene la ruta del archivo Excel desde el archivo config.properties
        String rutaExcel = ConfigManager.get("ruta.excel");


        ExcelManager excel = new ExcelManager();
        ConsolaView vista = new ConsolaView();
        BusquedaContactos controlador = new BusquedaContactos(excel, vista, rutaExcel);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            vista.mostrarMensaje("""
                    
                    ================================
                       CONSULTA DE OPERADOR - MENÚ
                    ================================
                    Selecciona una opción:
                    
                    1. Ejecutar proceso desde una fila específica
                    2. Reprocesar solo los NO DETECTADOS
                    3. Salir
                    """);

            vista.mostrarMensaje("Ingrese opción");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1 -> {
                    // VENTANA EMERGENTE AQUÍ
                    String input = JOptionPane.showInputDialog(
                            null,
                            "¿Desde qué fila deseas iniciar la ejecución?",
                            "Ingresar Fila",
                            JOptionPane.QUESTION_MESSAGE
                    );

                    if (input == null) {
                        vista.mostrarMensaje("Operación cancelada.");
                        break;
                    }

                    try {
                        int filaInicio = Integer.parseInt(input);
                        controlador.ejecutar(filaInicio);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Debes ingresar un número válido.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
                case 2 -> {
                    vista.mostrarMensaje("Reprocesando registros NO DETECTADOS...");
                    controlador.reprocesarNoDetectados();
                }

                case 3 -> {
                    vista.mostrarMensaje("Saliendo del sistema...");
                    return; // Finaliza el programa
                }

                default -> vista.mostrarMensaje("Opción inválida. Intenta nuevamente.");
            }
        }
    }
}


