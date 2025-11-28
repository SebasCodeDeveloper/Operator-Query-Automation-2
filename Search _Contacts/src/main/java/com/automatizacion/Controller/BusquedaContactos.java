package com.automatizacion.Controller;

import com.automatizacion.Locators.DoctorSimLocators;
import com.automatizacion.Model.ConfigManager;
import com.automatizacion.Model.ExcelManager;
import com.automatizacion.View.ConsolaView;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;
import java.util.List;

/**
 * DESCRIPCIÓN GENERAL DE LA CLASE:
 * La clase {@code BusquedaContactos} se encarga de realizar la automatización
 * del proceso de consulta del operador telefónico para una lista de números
 * almacenados en un archivo Excel.
 * <p>
 * PARA CADA NÚMERO ENCONTRADO:
 * - Ingresa al sitio web configurado.
 * - Envía el número al campo correspondiente.
 * - Lee el operador detectado en la página.
 * - Registra dicho operador nuevamente en el archivo Excel.
 * <p>
 * Esta clase utiliza Selenium WebDriver para la automatización del navegador.
 *
 * @author Jhoan Sebastian Peña
 * @version 1.0
 * @since 06/11/2025
 */

public class BusquedaContactos {

    private final ConsolaView vista;
    private final String rutaExcel;

    /**
     * Constructor principal que inicializa las dependencias necesarias.
     *
     * @param excelManager Maneja la lectura y escritura del archivo Excel.
     * @param vista        Clase responsable de mostrar mensajes en consola.
     * @param rutaExcel    Ruta del archivo Excel que contiene los números.
     */
    public BusquedaContactos(ExcelManager excelManager, ConsolaView vista, String rutaExcel) {
        this.vista = vista;
        this.rutaExcel = rutaExcel;
    }

    /**
     * MÉTODO PRINCIPAL DEL PROCESO DE AUTOMATIZACIÓN.
     * <p>
     * Este método realiza el flujo completo para procesar los números telefónicos
     * almacenados en el archivo Excel, consultarlos en la página web configurada
     * y registrar el operador detectado nuevamente en el mismo archivo.
     * <p>
     * FUNCIONALIDADES:
     * 1. Inicializa el navegador y abre la URL definida en la configuración.
     * 2. Lee todos los contactos desde el archivo Excel.
     * 3. Valida que la fila de inicio sea correcta.
     * 4. Recorre cada número desde la fila indicada hasta la última fila del Excel.
     * 5. Para cada número:
     * - Ingresa el número en el campo correspondiente de la web.
     * - Avanza al siguiente paso y espera la respuesta.
     * - Obtiene el operador detectado; si no se encuentra, registra "NO DETECTADO".
     * - Guarda ese resultado en la columna correspondiente del Excel.
     * 6. Cuando llega al final del archivo, el proceso se detiene automáticamente y cierra el navegador.
     *
     * @param filaInicio Número de fila (índice) desde donde comenzará el procesamiento.
     */
    public void ejecutar(int filaInicio) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        String url = ConfigManager.get("web.url");
        driver.get(url);

        try {
            List<String> contactos = ExcelManager.leerContactos(rutaExcel);

            if (filaInicio < 0 || filaInicio >= contactos.size()) {
                vista.mostrarError("La fila de inicio no es válida.");
                driver.quit();
                return;
            }

            for (int i = filaInicio; i < contactos.size(); i++) {

                String numero = contactos.get(i);
                System.out.println("➡ Procesando posición #: " + i + " | Número: " + numero);

                WebElement input = driver.findElement(DoctorSimLocators.INPUT_NUMERO);
                input.clear();
                input.sendKeys(numero);
                driver.findElement(DoctorSimLocators.BOTON_SIGUIENTE).click();
                Thread.sleep(4000);

                // Leer operador
                String operadorResult;
                try {
                    WebElement operadorElemento = driver.findElement(DoctorSimLocators.OPERADOR);
                    operadorResult = operadorElemento.getText().trim().toUpperCase();

                    if (operadorResult.isEmpty()) {
                        operadorResult = "NO DETECTADO";
                        driver.get(url);
                    }
                } catch (Exception e) {
                    operadorResult = "NO DETECTADO";
                }
                // Registrar en Excel
                ExcelManager.escribirOperador(rutaExcel, i + 1, operadorResult);
            }

            vista.mostrarMensaje("✅ Proceso completado correctamente.");
            driver.quit();

        } catch (Exception e) {
            vista.mostrarError("Error durante la ejecución: " + e.getMessage());
        }
    }

    /**
     * MÉTODO QUE REPROCESA LOS NÚMEROS MARCADOS COMO "NO DETECTADO".
     * <p>
     * Este método busca en el archivo Excel todas las filas donde el operador
     * quedó registrado como "NO DETECTADO" y vuelve a intentar obtener el operador
     * desde la página web. Su propósito es corregir lecturas fallidas sin necesidad
     * de ejecutar el proceso completo nuevamente.
     * <p>
     * FUNCIONALIDADES:
     * 1. Obtiene del Excel la lista de filas que contienen el valor "NO DETECTADO".
     * 2. Si no hay registros pendientes, el proceso finaliza inmediatamente.
     * 3. Inicializa el navegador y abre la URL configurada.
     * 4. Lee todos los contactos desde el Excel para obtener los números correspondientes.
     * 5. Para cada fila detectada:
     * - Recupera el número asociado.
     * - Lo ingresa nuevamente en la página.
     * - Reintenta la detección del operador.
     * - Si no se obtiene resultado, se vuelve a registrar "NO DETECTADO".
     * - Guarda el resultado actualizado en la misma fila del Excel.
     * 6. Cierra el navegador al finalizar el reprocesamiento.
     * <p>
     * Este método permite reducir errores de lectura causados por fallos temporales
     * de red, lentitud del sitio o problemas aleatorios durante el procesamiento inicial.
     */
    public void reprocesarNoDetectados() {

        List<Integer> filas = ExcelManager.obtenerFilasNoDetectados(rutaExcel);
        if (filas.isEmpty()) {
            vista.mostrarMensaje("✔ No hay números con operador 'NO DETECTADO'.");
            return;
        }

        vista.mostrarMensaje("🔁 Reprocesando " + filas.size() + " números no detectados...");

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        String url = ConfigManager.get("web.url");
        driver.get(url);

        try {
            List<String> contactos = ExcelManager.leerContactos(rutaExcel);

            for (int fila : filas) {
                String numero = contactos.get(fila - 1);
                vista.mostrarMensaje("Reintentando número: " + numero);


                WebElement input = driver.findElement(DoctorSimLocators.INPUT_NUMERO);
                input.clear();
                input.sendKeys(numero);
                driver.findElement(DoctorSimLocators.BOTON_SIGUIENTE).click();
                Thread.sleep(4000);

                String operadorResult;
                try {
                    WebElement operadorElemento = driver.findElement(DoctorSimLocators.OPERADOR);
                    operadorResult = operadorElemento.getText().trim().toUpperCase();

                    if (operadorResult.isEmpty()) {
                        operadorResult = "NO DETECTADO";
                        driver.get(url);
                    }

                } catch (Exception e) {
                    operadorResult = "NO DETECTADO";
                }

                ExcelManager.escribirOperador(rutaExcel, fila, operadorResult);
            }

            vista.mostrarMensaje("✔ Reprocesamiento finalizado.");

        } catch (Exception e) {
            vista.mostrarError("Error durante reprocesamiento: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }
}
