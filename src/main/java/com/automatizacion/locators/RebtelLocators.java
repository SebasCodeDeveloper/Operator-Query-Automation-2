package com.automatizacion.locators;

import org.openqa.selenium.By;

/**
 * Repositorio centralizado de selectores para el portal Rebtel.
 * <p>
 * Esta clase sigue elpatrón dediseño Object Repository para facilitar
 * el mantenimiento y la reutilización de elementos de la interfaz.
 * *@author SebasCodeDev *@version 1.3.1*@since 06/11/2025
 */

public class RebtelLocators {

    /**
     * Campo número
     */
    public static final By INPUT_NUMERO =
            By.id("send-bundle-phone-field");

    /**
     * Botón editar/reset
     */
    public static final By BTN_EDITAR =
            By.id("unified-product-list-reset-btn");

    /**
     * Recarga móvil
     */
    public static final By RECARGA_MOVIL =
            By.xpath("//*[@id=\"search-result-v1\"]/div/div[1]/div[2]");

    /**
     * Operador
     */
    public static final By OPERADOR =
            By.cssSelector(".operator-logo-singular-product");


    /**
     * Número inválido
     */
    public static final By ERROR_TELEFONO = By.id("phone-number-error-handler");

    /**
     * Loading infinito
     */
    public static final By LOADING =
            By.cssSelector(".search-result-bundle-block-loading.loading");
}