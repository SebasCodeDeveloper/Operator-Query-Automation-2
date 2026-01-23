package com.automatizacion;

import com.automatizacion.automation.DoctorSimAutomation;
import com.automatizacion.automation.DoctorSimAutomationImpl;
import com.automatizacion.controller.BusquedaContactosController;
import com.automatizacion.model.ConfigManager;
import com.automatizacion.model.ExcelManager;
import com.automatizacion.service.RedService;
import com.automatizacion.service.RedServiceImpl;
import com.automatizacion.view.ConsolaView;

import javax.swing.*;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        // 🔧 Configuración
        String rutaExcel = ConfigManager.get("ruta.excel");
        String url = ConfigManager.get("web.url");  // Aquí obtienes la URL de la config.properties

        // 🧱 Infraestructura
        ExcelManager excelManager = new ExcelManager();
        ConsolaView vista = new ConsolaView();

        DoctorSimAutomation automation = new DoctorSimAutomationImpl();
        RedService redService = new RedServiceImpl();

        // 🎮 Controller
        BusquedaContactosController controller =
                new BusquedaContactosController(
                        excelManager,
                        vista,
                        automation,
                        redService,
                        rutaExcel,
                        url
                );

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

            vista.mostrarMensaje("Ingrese opción:");
            int opcion = scanner.nextInt();

            switch (opcion) {

                case 1 -> {
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
                        int filaInicio = Integer.parseInt(input) - 1; // base 0
                        controller.ejecutar(filaInicio);
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
                    vista.mostrarMensaje("🔁 Reprocesando registros NO DETECTADOS...");
                    controller.reprocesarNoDetectados();
                }

                case 3 -> {
                    vista.mostrarMensaje("👋 Saliendo del sistema...");
                    return;
                }

                default -> vista.mostrarMensaje("❌ Opción inválida. Intenta nuevamente.");
            }
        }
    }
}
