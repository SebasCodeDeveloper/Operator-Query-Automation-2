package com.automatizacion.locators;

import org.openqa.selenium.By;

/**
 * Repositorio centralizado de selectores para el portal DoctorSim.
 * Esta clase sigue el patrón de diseño Object Repository para facilitar
 * el mantenimiento y la reutilización de elementos de la interfaz.
 *
 * @author SebasCodeDev
 * @version 1.3.1
 * @since 06/11/2025
 */
public class DoctorSimLocators {

    /**
     * Selector para el campo de entrada del número telefónico.
     */
    public static final By INPUT_NUMERO = By.id("phone");

    /**
     * Selector del contenedor que muestra el nombre del operador tras la búsqueda.
     */
    public static final By OPERADOR = By.xpath("//*[@id=\"showSelec\"]/div[2]/div/div/div/div/div/p");

    /**
     * Selector del mensaje emergente (modal) que indica errores o avisos del sistema.
     */
    public static final By MODAL_OPERADOR = By.xpath("//*[@id=\"message_select\"]/div/div/div[1]/center/span");

    /**
     * Botón de confirmación para cerrar los modales de advertencia.
     */
    public static final By BOTON_OK = By.xpath("//*[@id=\"message_select\"]/div/div/div[3]/a");

}