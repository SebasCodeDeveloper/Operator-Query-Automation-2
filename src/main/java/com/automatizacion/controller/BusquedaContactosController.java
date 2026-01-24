package com.automatizacion.controller;

import com.automatizacion.automation.DoctorSimAutomation;
import com.automatizacion.model.ExcelManager;
import com.automatizacion.model.ResultadoBusqueda;
import com.automatizacion.service.RedService;
import com.automatizacion.view.ConsolaView;

import java.util.List;

/**
 * Controller encargado de coordinar el flujo del proceso
 * de consulta y reprocesamiento de contactos.
 *
 * NO contiene lógica Selenium
 * NO contiene lógica de red
 * NO contiene lógica de infraestructura
 *
 * @author SebasCodeDev
 * @version 1.3.1
 */
public class BusquedaContactosController {

    private final ExcelManager excelManager;
    private final ConsolaView vista;
    private final DoctorSimAutomation automation;
    private final RedService redService;
    private final String rutaExcel;
    String url;

    /**
     * Constructor que recibe las dependencias y la configuración necesaria.
     */
    public BusquedaContactosController(
            ExcelManager excelManager,
            ConsolaView vista,
            DoctorSimAutomation automation,
            RedService redService,
            String rutaExcel,
            String url
    ) {
        this.excelManager = excelManager;
        this.vista = vista;
        this.automation = automation;
        this.redService = redService;
        this.rutaExcel = rutaExcel;
        this.url = url;
    }

    /**
     * Ejecuta el proceso completo desde una fila inicial.
     */
    public void ejecutar(int filaInicio) {

        List<String> contactos = excelManager.leerContactos(rutaExcel);

        // Validación de rango para evitar errores de índice
        if (filaInicio < 0 || filaInicio >= contactos.size()) {
            vista.mostrarError("La fila de inicio no es válida.");
            return;
        }

        vista.mostrarMensaje("🚀 Iniciando proceso desde fila " + (filaInicio + 1));

        // AQUÍ SE INICIALIZA EL DRIVER
        automation.iniciar(url);

        for (int i = filaInicio; i < contactos.size(); i++) {
            String numero = contactos.get(i);
            System.out.println("➡ Procesando fila " + (i + 1) + " | Número: " + numero);

            try {
                automation.ingresarNumero(numero);
                ResultadoBusqueda resultado = automation.consultarResultado();

                // Lógica de recuperación si se detecta un bloqueo (modal)
                if (resultado.hayModal()) {
                    vista.mostrarMensaje("🚩 Modal detectado. Cambiando red...");
                    // Se guarda operador no detectado
                    excelManager.escribirOperador(rutaExcel, i + 1, ResultadoBusqueda.noDetectado().getOperador());

                    // Reinicio de sesión con cambio de IP
                    automation.cerrar();
                    redService.alternarRed();
                    automation.iniciar(url);
                    automation.refrescar();
                    Thread.sleep(3000);
                    continue;
                }

                // Registro del operador encontrado en el Excel
                excelManager.escribirOperador(
                        rutaExcel,
                        i + 1,
                        resultado.getOperador()
                );

            } catch (Exception e) {
                vista.mostrarError("Error en fila " + (i + 1) + ": " + e.getMessage());
            }
        }

        automation.cerrar();
        vista.mostrarMensaje("✅ Proceso completado correctamente");
    }

    /**
     * Reprocesa únicamente las filas marcadas como NO DETECTADO.
     */
    public void reprocesarNoDetectados() {

        List<Integer> filas = excelManager.obtenerFilasNoDetectados(rutaExcel);

        if (filas.isEmpty()) {
            vista.mostrarMensaje("✔ No hay números 'NO DETECTADO'.");
            return;
        }

        vista.mostrarMensaje("🔁 Reprocesando " + filas.size() + " registros...");

        // Preparación del entorno para el reintento
        redService.alternarRed();
        automation.iniciar(url);

        List<String> contactos = excelManager.leerContactos(rutaExcel);

        for (int fila : filas) {
            String numero = contactos.get(fila - 1);
            System.out.println("🔁 Reintentando fila " + fila + " | Número: " + numero);

            try {
                automation.ingresarNumero(numero);
                ResultadoBusqueda resultado = automation.consultarResultado();

                if (resultado.hayModal()) {
                    vista.mostrarMensaje("🚩 Modal detectado. Cambiando red...");
                    excelManager.escribirOperador(rutaExcel, fila, ResultadoBusqueda.noDetectado().getOperador());

                    automation.cerrar();
                    redService.alternarRed();
                    automation.iniciar(url);
                    automation.refrescar();
                    Thread.sleep(3000);
                    continue;
                }

                excelManager.escribirOperador(
                        rutaExcel,
                        fila,
                        resultado.getOperador()
                );

            } catch (Exception e) {
                vista.mostrarError("Error en fila " + fila + ": " + e.getMessage());
            }
        }

        automation.cerrar();
        vista.mostrarMensaje("✅ Reprocesamiento completado");
    }
}