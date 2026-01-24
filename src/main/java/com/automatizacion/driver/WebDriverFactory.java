package com.automatizacion.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Fábrica encargada de centralizar la creación y configuración
 * de la instancia de WebDriver para Chrome.
 * * @author SebasCodeDev
 * @version 1.3.1
 */
public class WebDriverFactory {

    /**
     * Configura el driver, define opciones de inicio y navega a la URL base.
     * @param url Dirección web que el navegador cargará al iniciar.
     * @return Una instancia de WebDriver (ChromeDriver) lista para usar.
     */
    public static WebDriver crear(String url) {
        // Configura automáticamente el binario de ChromeDriver según la versión del navegador
        WebDriverManager.chromedriver().setup();

        // Configuración de parámetros de inicio para Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--remote-allow-origins=*");

        // Inicialización del driver con las opciones definidas
        WebDriver driver = new ChromeDriver(options);

        // Navegación inmediata a la URL proporcionada
        driver.get(url);

        return driver;
    }
}