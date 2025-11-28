package com.automatizacion.Locators;

import org.openqa.selenium.By;

/**
 * DESCRIPCION GENRAL DE LA CLASE:
 * <p>
 * Clase {@code DoctorSimLocators}  contiene los localizadores (By) utilizados en la página de consulta
 * del operador en DoctorSIM.
 * <p>
 * Estos localizadores permiten a Selenium identificar los elementos necesarios para:
 * Ingresar el número telefónico.
 * Iniciar la consulta.
 * Leer el operador detectado.
 * <p>
 * Se recomienda mantener esta clase exclusiva para localizadores,
 * evitando lógica o interacciones con la UI.
 *
 * @author Jhoan
 * @version 1.0
 * @since 06/11/2025
 */
public class DoctorSimLocators {

    /**
     * Campo de texto donde se ingresa el número telefónico.
     */
    public static final By INPUT_NUMERO = By.id("phone");

    /**
     * Elemento donde se muestra el operador después de realizar la consulta.
     */
    public static final By OPERADOR = By.xpath("//*[@id=\"showSelec\"]/div[2]/div/div/div/div/div/p");

}
