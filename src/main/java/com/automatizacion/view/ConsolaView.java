package com.automatizacion.view;

/**
 * Clase encargada de la interfaz de usuario por consola.
 * Actúa como la capa de salida (Vista) en el patrón MVC, centralizando
 * la comunicación de eventos y errores durante la ejecución del bot.
 *
 * @author SebasCodeDev
 * @version 1.3.1
 * @since 01/24/2026
 */
public class ConsolaView {

    /**
     * Imprime un mensaje informativo estándar en la salida del sistema.
     *
     * @param mensaje Texto explicativo del paso actual del proceso.
     */
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    /**
     * Imprime una alerta de error en la salida de errores (stderr).
     * Incluye un formato visual para resaltar fallos críticos o excepciones.
     *
     * @param error Descripción detallada del problema encontrado.
     */
    public void mostrarError(String error) {
        System.err.println("⚠ ERROR: " + error);
    }
}