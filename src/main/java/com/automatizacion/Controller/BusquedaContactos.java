package com.automatizacion.Controller;

import com.automatizacion.Locators.DoctorSimLocators;
import com.automatizacion.Model.ConfigManager;
import com.automatizacion.Model.ExcelManager;
import com.automatizacion.View.ConsolaView;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.jspecify.annotations.NonNull;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

/**
 * @author Jhoan Sebastian Peña
 * @version 1.8 - Con seguimiento de proceso y cambio de red funcional
 */
public class BusquedaContactos {

    private final ConsolaView vista;
    private final String rutaExcel;
    private final Random random = new Random();
    private String redActual = " claro ";
    String url = ConfigManager.get("web.url");

    public BusquedaContactos(ExcelManager excelManager, ConsolaView vista, String rutaExcel) {
        this.vista = vista;
        this.rutaExcel = rutaExcel;
    }

    public void ejecutar(int filaInicio) {
        WebDriverManager.chromedriver().setup();


        // 📶 Cambio de red inicial
        alternarRedWifi();
        WebDriver driver = crearDriverLimpio(url);

        try {
            List<String> contactos = ExcelManager.leerContactos(rutaExcel);

            if (filaInicio < 0 || filaInicio >= contactos.size()) {
                vista.mostrarError("La fila de inicio no es válida.");
                return;
            }

            for (int i = filaInicio; i < contactos.size(); i++) {

                // ☕ PAUSA HUMANA LARGA CADA 10 NÚMEROS
                if (i > 0 && i % 10 == 0) {
                    System.out.println("☕ Pausa humana larga");
                    pausaHumana(5000, 8000); //Tiempo anterior 10000, 20000
                }

                // 🔄 REINICIO PREVENTIVO Y CAMBIO DE RED CADA 15
                if (i > filaInicio && i % 15 == 0) {
                    System.out.println("🔄 Reinicio preventivo del navegador y cambio de red");
                    driver.quit();
                    alternarRedWifi();
                    pausaHumana(3000, 5000); //Tiempo anterior 4000, 7000
                    driver = crearDriverLimpio(url);
                }

                // 📢 MENSAJE DE SEGUIMIENTO (RESTABLECIDO)
                String numero = contactos.get(i);
                System.out.println("➡ Procesando fila " + (i + 1) + " | Número: " + numero);

                try {
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
                    WebElement input = wait.until(ExpectedConditions.elementToBeClickable(DoctorSimLocators.INPUT_NUMERO));

                    input.clear();
                    pausaHumana(800, 1500);
                    input.sendKeys(numero);
                    pausaHumana(600, 1200);
                    input.sendKeys(Keys.ENTER);

                } catch (TimeoutException e) {
                    System.out.println("⚠ Input bloqueado en fila " + (i + 1) + ", recargando...");
                    driver.navigate().refresh();
                    pausaHumana(4000, 7000);
                    ExcelManager.escribirOperador(rutaExcel, i + 1, "NO RESPONDE");
                    continue;
                }

                String operadorResult = "NO DETECTADO";

                try {
                    WebDriverWait waitRes = new WebDriverWait(driver, Duration.ofSeconds(10)); //25
                    WebElement operador = waitRes.until(ExpectedConditions.visibilityOfElementLocated(DoctorSimLocators.OPERADOR));
                    operadorResult = operador.getText().trim().toUpperCase();


                } catch (TimeoutException e) {
                    // Verificación de modal
                    if (!driver.findElements(DoctorSimLocators.MODAL_OPERADOR).isEmpty()) {
                        System.out.println("🚩 Modal detectado en fila " + (i + 1) + ". Reiniciando red...");
                        try {
                            driver.findElement(DoctorSimLocators.BOTON_OK).click();
                        } catch (Exception ignored) {
                        }

                        driver.quit();
                        alternarRedWifi();
                        pausaHumana(1500, 2500); //Tiempo anterior 5000, 8000
                        driver = crearDriverLimpio(url);
                    } else {
                        System.out.println("⏳ Tiempo agotado en fila " + (i + 1) + ", refrescando...");
                        driver.navigate().refresh();
                        pausaHumana(4000, 7000);
                    }
                }

                ExcelManager.escribirOperador(rutaExcel, i + 1, operadorResult);
            }

            vista.mostrarMensaje("✅ Proceso completado correctamente");

        } catch (Exception e) {
            vista.mostrarError("Error crítico: " + e.getMessage());
        } finally {
            if (driver != null) driver.quit();
        }
    }

    public void reprocesarNoDetectados() {
        List<Integer> filas = ExcelManager.obtenerFilasNoDetectados(rutaExcel);
        if (filas.isEmpty()) {
            vista.mostrarMensaje("✔ No hay números 'NO DETECTADO'.");
            return;
        }

        vista.mostrarMensaje("🔁 Reprocesando " + filas.size() + " números...");
        alternarRedWifi();
        WebDriverManager.chromedriver().setup();
        WebDriver driver = crearDriverLimpio(ConfigManager.get("web.url"));

        try {
            List<String> contactos = ExcelManager.leerContactos(rutaExcel);

            for (int fila : filas) {
                String numero = contactos.get(fila - 1);
                System.out.println("🔁 Reintentando Fila " + fila + " | Número: " + numero);

                try {
                    WebElement input = driver.findElement(DoctorSimLocators.INPUT_NUMERO);
                    input.clear();
                    pausaHumana(800, 1500);
                    input.sendKeys(numero);
                    pausaHumana(600, 1200);
                    input.sendKeys(Keys.ENTER);

                    String r = "NO DETECTADO";

                    try {
                        WebDriverWait waitRes = new WebDriverWait(driver, Duration.ofSeconds(5)); //Antes 10
                        WebElement operador = waitRes.until(
                                ExpectedConditions.visibilityOfElementLocated(DoctorSimLocators.OPERADOR));
                        r = operador.getText().trim().toUpperCase();

                    } catch (TimeoutException e) {

                        // 👉 SI APARECE EL MODAL
                        if (!driver.findElements(DoctorSimLocators.MODAL_OPERADOR).isEmpty()) {
                            System.out.println("🚩 Modal detectado en fila " + fila + ". Reiniciando red...");

                            try {
                                driver.findElement(DoctorSimLocators.BOTON_OK).click();
                            } catch (Exception ignored) {
                            }

                            driver.quit();
                            alternarRedWifi();
                            pausaHumana(1500, 2500);
                            driver = crearDriverLimpio(ConfigManager.get("web.url"));
                            // 🔴 CLAVE: saltar a la siguiente fila
                            continue;
                        }

                        // 👉 SI NO HUBO MODAL, solo timeout
                        System.out.println("⏳ Tiempo agotado en fila " + fila + ", refrescando...");
                        driver.navigate().refresh();
                        pausaHumana(4000, 7000);
                    }

                    ExcelManager.escribirOperador(rutaExcel, fila, r);
                    System.out.println("   -> Nuevo resultado: " + r);

                } catch (Exception e) {
                    System.out.println("❌ Error procesando fila " + fila + ": " + e.getMessage());
                }
            }

            vista.mostrarMensaje("✅ Proceso completado correctamente");

        } catch (Exception e) {
            System.out.println("   -> Error en reintento general. Reiniciando red...");
        } finally {
            if (driver != null) driver.quit();
        }
    }


    private void alternarRedWifi() {
        if (this.redActual.equals(" claro ")) {
            this.redActual = " claro  2";
        } else {
            this.redActual = " claro ";
        }

        try {
            System.out.println("📶 [WIFI] Cambiando a: [" + redActual + "]");

            Process p1 = Runtime.getRuntime().exec("netsh wlan disconnect interface=\"Wi-Fi\"");
            p1.waitFor();
            Thread.sleep(2000);

            String[] cmdConectar = {"cmd.exe", "/c", "netsh wlan connect name=\"" + redActual + "\" interface=\"Wi-Fi\""};
            Process p2 = Runtime.getRuntime().exec(cmdConectar);
            p2.waitFor();

            System.out.println("⏳ [WIFI] Esperando 2s para estabilizar conexión...");
            Thread.sleep(2000); //Tiempo anterior 8000,
        } catch (Exception e) {
            System.err.println("❌ Error en cambio de red: " + e.getMessage());
        }
    }


    private @NonNull WebDriver crearDriverLimpio(String url) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);
        driver.get(url);
        return driver;
    }

    private void pausaHumana(int minMs, int maxMs) throws InterruptedException {
        Thread.sleep(minMs + random.nextInt(maxMs - minMs));
    }
}