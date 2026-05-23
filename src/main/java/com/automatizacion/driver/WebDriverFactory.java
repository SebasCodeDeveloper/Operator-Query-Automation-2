package com.automatizacion.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.*;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * Factory para crear el WebDriver optimizado.
 *
 * OPTIMIZACIONES:
 * Headless rápido
 * Sin imágenes (más velocidad)
 * Menos consumo de recursos
 */
public class WebDriverFactory {

    public static WebDriver crear(String url) {

        // Configura driver automáticamente
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();

        options.addArguments(
                "--disable-gpu",
                "--no-sandbox",
                "--disable-dev-shm-usage",
            //    "--blink-settings=imagesEnabled=false",
                "--remote-allow-origins=*"
        );

        WebDriver driver = new ChromeDriver(options);


        driver.get(url);

        return driver;
    }

/**
 * Limpia la caché de los binarios y resoluciones de WebDriverManager.
 * Útil si experimentas problemas de compatibilidad tras actualizaciones de Chrome.
 */
        public static void limpiarCache() {
            // Limpia los binarios descargados (chromedriver.exe, etc.)
            WebDriverManager.chromedriver().clearDriverCache();

            // Limpia el historial de versiones/resoluciones
            WebDriverManager.chromedriver().clearResolutionCache();

            System.out.println("✅ Caché de WebDriverManager limpiada correctamente.");
        }
    }