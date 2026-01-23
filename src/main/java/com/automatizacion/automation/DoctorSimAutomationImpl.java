package com.automatizacion.automation;

import com.automatizacion.locators.DoctorSimLocators;
import com.automatizacion.model.ResultadoBusqueda;
import com.automatizacion.driver.WebDriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DoctorSimAutomationImpl implements DoctorSimAutomation {

    private WebDriver driver;

    /**
     * Inicializa el WebDriver usando la fábrica y abre la URL.
     */
    @Override
    public void iniciar(String url) {
        driver = WebDriverFactory.crear(url);
    }

    @Override
    public void ingresarNumero(String numero) throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement input = wait.until(
                ExpectedConditions.elementToBeClickable(DoctorSimLocators.INPUT_NUMERO)
        );

        input.clear();
        Thread.sleep(800);
        input.sendKeys(numero);
        Thread.sleep(600);
        input.sendKeys(Keys.ENTER);
    }

    @Override
    public ResultadoBusqueda consultarResultado() {
        try {
            WebDriverWait waitRes = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement operador = waitRes.until(
                    ExpectedConditions.visibilityOfElementLocated(DoctorSimLocators.OPERADOR)
            );

            String resultado = operador.getText().trim().toUpperCase();
            return ResultadoBusqueda.ok(resultado);

        } catch (TimeoutException e) {

            // Detectar modal
            if (!driver.findElements(DoctorSimLocators.MODAL_OPERADOR).isEmpty()) {
                try {
                    driver.findElement(DoctorSimLocators.BOTON_OK).click();
                } catch (Exception ignored) {}

                return ResultadoBusqueda.modal();
            }

            return ResultadoBusqueda.noDetectado();
        }
    }

    @Override
    public void refrescar() {
        driver.navigate().refresh();
    }

    @Override
    public void cerrar() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
