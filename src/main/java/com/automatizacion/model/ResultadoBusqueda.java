package com.automatizacion.model;

/**
 * Modelo que representa el resultado de una consulta en la plataforma.
 * Encapsula la información del operador obtenido y el estado de la interfaz (modales).
 * * @author SebasCodeDev
 * @version 1.3.1
 * @since 01/24/2026
 */
public class ResultadoBusqueda {

    private final String operador;
    private final boolean modal;

    /**
     * Constructor privado para forzar el uso de métodos de fábrica estáticos.
     * @param operador Nombre del operador identificado.
     * @param modal Indica si se detectó una interrupción por ventana emergente.
     */
    private ResultadoBusqueda(String operador, boolean modal) {
        this.operador = operador;
        this.modal = modal;
    }

    /**
     * Crea un resultado exitoso con el nombre del operador.
     * @param operador Texto del operador detectado.
     * @return Instancia de ResultadoBusqueda con éxito.
     */
    public static ResultadoBusqueda ok(String operador) {
        return new ResultadoBusqueda(operador, false);
    }

    /**
     * Crea un resultado que indica la presencia de un bloqueo por modal.
     * @return Instancia de ResultadoBusqueda con estado de modal activo.
     */
    public static ResultadoBusqueda modal() {
        return new ResultadoBusqueda(null, true);
    }

    /**
     * Crea un resultado para casos donde la consulta no arrojó datos claros.
     * @return Instancia de ResultadoBusqueda marcada como "NO DETECTADO".
     */
    public static ResultadoBusqueda noDetectado() {
        return new ResultadoBusqueda("NO DETECTADO", false);
    }

    /**
     * Verifica si el flujo fue interrumpido por un modal de error o aviso.
     * @return true si hay un modal presente, false de lo contrario.
     */
    public boolean hayModal() {
        return modal;
    }

    /**
     * Obtiene el nombre del operador almacenado.
     * @return Cadena con el nombre del operador o "NO DETECTADO".
     */
    public String getOperador() {
        return operador;
    }
}