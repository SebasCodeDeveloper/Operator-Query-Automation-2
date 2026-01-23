package com.automatizacion.automation;

import com.automatizacion.model.ResultadoBusqueda;

public interface DoctorSimAutomation {

    void iniciar(String url);

    void ingresarNumero(String numero) throws Exception;

    ResultadoBusqueda consultarResultado();

    void refrescar();

    void cerrar();
}
