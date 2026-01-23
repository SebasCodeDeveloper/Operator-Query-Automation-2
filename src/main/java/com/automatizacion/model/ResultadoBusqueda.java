package com.automatizacion.model;

public class ResultadoBusqueda {

    private final String operador;
    private final boolean modal;

    private ResultadoBusqueda(String operador, boolean modal) {
        this.operador = operador;
        this.modal = modal;
    }

    public static ResultadoBusqueda ok(String operador) {
        return new ResultadoBusqueda(operador, false);
    }

    public static ResultadoBusqueda modal() {
        return new ResultadoBusqueda(null, true);
    }

    public static ResultadoBusqueda noDetectado() {
        return new ResultadoBusqueda("NO DETECTADO", false);
    }

    public boolean hayModal() {
        return modal;
    }

    public String getOperador() {
        return operador;
    }
}
