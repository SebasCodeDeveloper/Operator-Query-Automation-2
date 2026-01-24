package com.automatizacion.automation;

import com.automatizacion.locators.DoctorSimLocators;
import com.automatizacion.model.ResultadoBusqueda;
import com.automatizacion.driver.WebDriverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Clase que implementa la lógica de interacción con el portal DoctorSim.
 * Centraliza el manejo de elementos web y control de flujo de la búsqueda.
 * @author SebasCodeDev
 * @version 1.3.1
 */
public class DoctorSimAutomationImpl implements DoctorSimAutomation {

    private WebDriver driver;

    /**
     * Crea la instancia del navegador y navega a la página objetivo.
     */
    @Override
    public void iniciar(String url) {
        // Se utiliza el Factory para obtener un driver configurado
        driver = WebDriverFactory.crear(url);
    }

    /**
     * Proceso de ingreso de datos en el buscador.
     */
    @Override
    public void ingresarNumero(String numero) throws InterruptedException {
        // Espera máxima de 20 segundos para que el input sea interactuable
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Localiza el campo de texto del número
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(DoctorSimLocators.INPUT_NUMERO));

        // Limpia cualquier residuo de texto antes de escribir
        input.clear();

        // Pequeñas pausas para asegurar que la página procese los eventos de teclado
        Thread.sleep(800);
        input.sendKeys(numero);

        Thread.sleep(600);
        // Simula la tecla Enter para disparar la consulta
        input.sendKeys(Keys.ENTER);
    }

    /**
     * Evalúa si la consulta fue exitosa o si aparecieron obstáculos.
     */
    @Override
    public ResultadoBusqueda consultarResultado() {
        try {
            // Intenta localizar el texto del operador con una espera corta
            WebDriverWait waitRes = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement operador = waitRes.until(ExpectedConditions.visibilityOfElementLocated(DoctorSimLocators.OPERADOR));

            // Retorna el resultado formateado en mayúsculas
            return ResultadoBusqueda.ok(operador.getText().trim().toUpperCase());

        } catch (TimeoutException e) {
            // Si no aparece el resultado, verifica si hay un modal de error visible
            if (!driver.findElements(DoctorSimLocators.MODAL_OPERADOR).isEmpty()) {
                try {
                    // Intenta cerrar el modal para no bloquear futuras ejecuciones
                    driver.findElement(DoctorSimLocators.BOTON_OK).click();
                } catch (Exception ignored) {
                    // Si falla el clic, se ignora para continuar con el flujo
                }
                return ResultadoBusqueda.modal();
            }
            // Si no hay modal ni resultado, se marca como no detectado
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
            // Limpia la referencia para evitar fugas de memoria
            driver = null;
        }
    }
}