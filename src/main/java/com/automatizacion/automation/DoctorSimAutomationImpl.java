package com.automatizacion.automation;

import com.automatizacion.locators.DoctorSimLocators;
import com.automatizacion.model.ResultadoBusqueda;
import com.automatizacion.driver.WebDriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Clase que implementa la lógica de interacción con el portal DoctorSim. * Centraliza el manejo de elementos web y control de flujo de la búsqueda. * @author SebasCodeDev * @version 1.3.1
 */
public class DoctorSimAutomationImpl implements DoctorSimAutomation {
    private WebDriver driver;

    /**
     * Crea la instancia del navegador y navega a la página objetivo.
     */
    @Override
    public void iniciar(String url) {

        driver = WebDriverFactory.crear(url);
    }

    /**
     * Proceso de ingreso de datos en el buscador.
     */
    @Override
    public void ingresarNumero(String numero) throws InterruptedException {
        // Espera máxima de 30 segundos para que el input sea interactuable
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
        // Localiza el campo de texto del número
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(DoctorSimLocators.INPUT_NUMERO));
        // Limpia cualquier residuo de texto antes de escribir
        pausaHumana(100, 200);
        input.clear();
        pausaHumana(1000, 1100); //1000, 1100
        input.sendKeys(numero);
        pausaHumana(900, 1100); //900, 1100
        // Simula la tecla Enter para disparar la consulta
        input.sendKeys(Keys.ENTER); }

    /**
     * Evalúa si la consulta fue exitosa o si aparecieron obstáculos.
     */
    @Override
    public ResultadoBusqueda consultarResultado() {
        try {
            if (!driver.findElements(DoctorSimLocators.ERROR_TELEFONO).isEmpty()) {
                WebElement divError = driver.findElement(DoctorSimLocators.ERROR_TELEFONO);
                if (divError.isDisplayed() && divError.getText().contains("Revisa el número")) {
                    System.out.println("❌ Número no válido detectado.");
                    return ResultadoBusqueda.noDetectado();
                }
            }
            WebDriverWait waitRes = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement operadorElemento = waitRes.until(ExpectedConditions.visibilityOfElementLocated(DoctorSimLocators.OPERADOR));
            waitRes.until(driver -> !operadorElemento.getText().trim().isEmpty());
            pausaHumana(950, 1000); //1100, 1200

            String textoOperador = operadorElemento.getText().trim().toUpperCase();
            return ResultadoBusqueda.ok(textoOperador);
        } catch (TimeoutException e) {
            if (!driver.findElements(DoctorSimLocators.MODAL_OPERADOR).isEmpty()) {
                try {
                    driver.findElement(DoctorSimLocators.BOTON_OK).click();
                } catch (Exception ignored) {
                }
                return ResultadoBusqueda.modal();
            }
            return ResultadoBusqueda.noDetectado();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResultadoBusqueda.noDetectado();
        }
    }

    /**
     * Recarga la página actual del navegador.
     */
    @Override
    public void refrescar() {
        driver.navigate().refresh();
    }

    /**
     * Finaliza la sesión del navegador de forma segura.
     */
    @Override
    public void cerrar() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public void pausaHumana(int min, int max) throws InterruptedException {
        long randomSleep = min + (long)(Math.random() * (max - min));
        Thread.sleep(randomSleep);
    }
}