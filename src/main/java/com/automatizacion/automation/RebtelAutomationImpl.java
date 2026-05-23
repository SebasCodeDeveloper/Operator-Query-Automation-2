package com.automatizacion.automation;

import com.automatizacion.driver.WebDriverFactory;
import com.automatizacion.locators.RebtelLocators;
import com.automatizacion.model.ResultadoBusqueda;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Clase que implementa la lógica de interacción con el portal Rebtel.
 * Centraliza el manejo de elementos web y control de flujo de la búsqueda.
 *
 * @author SebasCodeDev
 * @version 1.4.0
 */
public class RebtelAutomationImpl implements RebtelAutomation {
    private WebDriver driver;

    /**
     * Crea la instancia del navegador y navega a la página objetivo.
     */
    @Override
    public void iniciar(String url) {

        driver = WebDriverFactory.crear(url);

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement recargaMovil = wait.until(ExpectedConditions.elementToBeClickable(RebtelLocators.RECARGA_MOVIL));
            recargaMovil.click();
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo hacer click en RECARGA_MOVIL");
        }
    }

    /**
     * Proceso de ingreso de datos en el buscador.
     */
    @Override
    public void ingresarNumero(String numero) throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(RebtelLocators.INPUT_NUMERO));
        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);
        input.sendKeys(numero);
        input.sendKeys(Keys.ENTER);
    }

    /**
     * Evalúa si la consulta fue exitosa o si aparecieron obstáculos.
     */
    @Override
    public ResultadoBusqueda consultarResultado() {
        try {
            try {
                WebDriverWait waitError = new WebDriverWait(driver, Duration.ofSeconds(2));
                waitError.until(ExpectedConditions.visibilityOfElementLocated(RebtelLocators.ERROR_TELEFONO));
            } catch (TimeoutException ignored) {
            }

            // Número inválido (Ahora sí tendrá el texto renderizado si el número era malo)
            if (!driver.findElements(RebtelLocators.ERROR_TELEFONO).isEmpty()) {
                WebElement divError = driver.findElement(RebtelLocators.ERROR_TELEFONO);
                String textoError = divError.getAttribute("textContent");

                if (divError.isDisplayed() && textoError != null && textoError.contains("invalid")) {
                    System.out.println("❌ Número no válido");

                    try {
                        WebElement input = driver.findElement(RebtelLocators.INPUT_NUMERO);
                        input.sendKeys(Keys.CONTROL + "a");
                        input.sendKeys(Keys.DELETE);
                    } catch (Exception ignored) {}

                    return ResultadoBusqueda.noDetectado();
                }
            }

            // Si el número pasó la prueba del error, esperamos el operador normalmente
            WebDriverWait waitRes = new WebDriverWait(driver, Duration.ofSeconds(10));
            // Espera operador
            WebElement operadorElemento = waitRes.until(ExpectedConditions.visibilityOfElementLocated(RebtelLocators.OPERADOR));
            pausaHumana(300, 500);
            String textoOperador = operadorElemento.getAttribute("alt").trim().toUpperCase();

            WebElement btnEditar = waitRes.until(ExpectedConditions.elementToBeClickable(RebtelLocators.BTN_EDITAR));
            btnEditar.click();

            return ResultadoBusqueda.ok(textoOperador);
        } catch (TimeoutException e) {
            if (loadingInfinito()) {
                System.out.println("🚨 Loading infinito detectado.");
                return ResultadoBusqueda.modal();
            }
            return ResultadoBusqueda.noDetectado();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ResultadoBusqueda.noDetectado();
        }
    }

    /**
     * Detecta loading infinito
     */
    public boolean loadingInfinito() {

        try {
            // Si aparece spinner/loading
            if (!driver.findElements(RebtelLocators.LOADING).isEmpty()) {
                System.out.println("⚠️ Loading detectado... aplicando pausa humana de 1 a 2 segundos");
                pausaHumana(1000, 2000);

                WebElement loading = driver.findElement(RebtelLocators.LOADING);
                if (loading.isDisplayed()) {
                    System.out.println("❌ Loading infinito detectado.");
                    return true;
                }
            }
        } catch (Exception ignored) {
        }
        return false;
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

    /**
     * Pausa humana
     */
    public void pausaHumana(int min, int max)
            throws InterruptedException {
        long randomSleep = min + (long) (Math.random() * (max - min));
        Thread.sleep(randomSleep);
    }
}