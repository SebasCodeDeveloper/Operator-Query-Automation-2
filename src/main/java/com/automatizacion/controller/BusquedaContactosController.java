package com.automatizacion.controller;

import com.automatizacion.automation.DoctorSimAutomation;
import com.automatizacion.model.ExcelManager;
import com.automatizacion.model.ResultadoBusqueda;
import com.automatizacion.service.RedService;
import com.automatizacion.view.ConsolaView;

import java.util.List;

/**
 * Controlador principal del flujo. * * 🔥 OPTIMIZACIONES: * - Estructura try-finally para garantizar el cierre de procesos. * - Flujo robusto que evita procesos huérfanos (zombies).
 */
public class BusquedaContactosController {
    private final ConsolaView vista;
    private final DoctorSimAutomation automation;
    private final RedService redService;
    private final String rutaExcel;
    String url;

    public BusquedaContactosController(ExcelManager excelManager, ConsolaView vista, DoctorSimAutomation automation, RedService redService, String rutaExcel, String url) {
        this.vista = vista;
        this.automation = automation;
        this.redService = redService;
        this.rutaExcel = rutaExcel;
        this.url = url;
    }

    /**
     * Ejecuta el proceso principal desde una fila específica.
     */
    public void ejecutar(int filaInicio) {
        List<String> contactos = ExcelManager.leerContactos(rutaExcel);
        if (filaInicio < 0 || filaInicio >= contactos.size()) {
            vista.mostrarError("La fila de inicio no es válida.");
            return;
        }
        vista.mostrarMensaje("🚀 Iniciando proceso desde fila " + (filaInicio + 1));
        automation.iniciar(url);
        try {
            for (int i = filaInicio; i < contactos.size(); i++) {
                String numero = contactos.get(i);
                System.out.println("➡ Procesando fila " + (i + 1) + " | Número: " + numero);
                try {
                    automation.ingresarNumero(numero);
                    ResultadoBusqueda resultado = automation.consultarResultado();
                    // 🚩 BLOQUEO DETECTADO
                    if (resultado.hayModal()) {
                        vista.mostrarMensaje("🚩 Modal detectado. Cambiando red...");
                        ExcelManager.escribirOperador(rutaExcel, i + 1, ResultadoBusqueda.noDetectado().getOperador());
                        automation.cerrar();
                        redService.alternarRed();
                        automation.iniciar(url);
                        automation.refrescar();
                        continue;
                    }
                    // Guarda resultado normal
                    ExcelManager.escribirOperador(rutaExcel, i + 1, resultado.getOperador());
                } catch (Exception e) {
                    vista.mostrarError("Error en fila " + (i + 1) + ": " + e.getMessage());
                }
            }
        } finally {
            // ESTO SE EJECUTA SIEMPRE AL FINALIZAR
            automation.cerrar();
            vista.mostrarMensaje("✅ Proceso completado y procesos cerrados.");
        }
    }

    /**
     * Reprocesa únicamente las filas marcadas como NO DETECTADO.
     */
    public void reprocesarNoDetectados() {
        List<Integer> filas = ExcelManager.obtenerFilasNoDetectados(rutaExcel);
        if (filas.isEmpty()) {
            vista.mostrarMensaje("✔ No hay números 'NO DETECTADO'.");
            return;
        }
        vista.mostrarMensaje("🔁 Reprocesando " + filas.size() + " registros...");
        automation.iniciar(url);
        List<String> contactos = ExcelManager.leerContactos(rutaExcel);
        try {
            for (int fila : filas) {
                String numero = contactos.get(fila - 1);
                System.out.println("🔁 Reintentando fila " + fila + " | Número: " + numero);
                try {
                    automation.ingresarNumero(numero);
                    ResultadoBusqueda resultado = automation.consultarResultado();
                    if (resultado.hayModal()) {
                        vista.mostrarMensaje("🚩 Modal detectado. Cambiando red...");
                        ExcelManager.escribirOperador(rutaExcel, fila, ResultadoBusqueda.noDetectado().getOperador());
                        automation.cerrar();
                        redService.alternarRed();
                        automation.iniciar(url);
                        automation.refrescar();
                        continue;
                    }
                    ExcelManager.escribirOperador(rutaExcel, fila, resultado.getOperador());
                } catch (Exception e) {
                    vista.mostrarError("Error en fila " + fila + ": " + e.getMessage());
                }
            }
        } finally {
            // ESTO SE EJECUTA SIEMPRE AL FINALIZAR
            automation.cerrar();
            vista.mostrarMensaje("✅ Reprocesamiento completado y procesos cerrados.");
        }
    }
}