package com.automatizacion.automation;

import com.automatizacion.model.ResultadoBusqueda;

/**
 * Interfaz que define el contrato para la automatización de Rebtel. * * @author SebasCodeDev * @version 1.3.1
 */
public interface RebtelAutomation {
    /**
     * Inicia el navegador y carga la URL del servicio.
     */
    void iniciar(String url);

    /**
     * Ingresa el número en el formulario y ejecuta la búsqueda.
     */
    void ingresarNumero(String numero) throws Exception;

    /**
     * Extrae el resultado obtenido tras la consulta.
     */
    ResultadoBusqueda consultarResultado() throws InterruptedException;

    /**
     * Refresca la página para limpiar la sesión actual.
     */
    void refrescar();

    /**
     * Cierra el navegador y libera recursos.
     */
    void cerrar();
}