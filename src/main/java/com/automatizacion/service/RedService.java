package com.automatizacion.service;

/**
 * Interfaz que define las operaciones de gestión de conectividad para el bot. * Proporciona mecanismos para mitigar bloqueos mediante la rotación de red. * * @author SebasCodeDev * @version 1.3.1 * @since 01/24/2026
 */
public interface RedService {

    /**
     * Ejecuta el proceso de desconexión y reconexión para obtener una nueva identidad de red. * Este método es vital para evitar detecciones de tráfico automatizado y superar modales de error.
     */
    void alternarRed();
}