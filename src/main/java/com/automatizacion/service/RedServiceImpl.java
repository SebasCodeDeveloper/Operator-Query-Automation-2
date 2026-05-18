package com.automatizacion.service;

import java.io.File;

/**
 * Implementación del servicio de red mediante comandos nativos de Windows.
 * Gestiona la alternancia entre redes inalámbricas y la reconfiguración de túneles VPN
 * para asegurar la rotación de identidades de red.
 *
 * 🔥 MEJORA:
 * Se evita que ProtonVPN abra ventana visible (modo oculto).
 *
 * @author SebasCodeDev
 * @version 1.4.0
 */
public class RedServiceImpl implements RedService {

    /**
     * Almacena el identificador (SSID) de la red inalámbrica actualmente en uso.
     */
    private String redActual = "MICHER";

    /**
     * Ruta absoluta del ejecutable del cliente VPN configurado en el sistema.
     */
    private final String rutaLauncher;

    /**
     * Constructor del servicio
     */
    public RedServiceImpl(String rutaLauncher) {
        this.rutaLauncher = rutaLauncher;
    }

    /**
     * Ejecuta el ciclo completo de alternancia de red y reconexión de seguridad.
     */
    @Override
    public void alternarRed() {

        // 🔁 Cambio entre redes
        if (redActual.equals("MICHER")) {
            redActual = "MICHER_5G";
        } else {
            redActual = "MICHER";
        }

        try {
            System.out.println("📶 [WIFI] Cambiando a: [" + redActual + "]");
            System.out.println("🛑 [VPN] Cerrando procesos y limpiando caché de red...");

            // 🔥 Cerrar Proton + limpiar red
            String cmdLimpiar = "powershell.exe -Command \""
                    + "Get-Process Proton* -ErrorAction SilentlyContinue | Stop-Process -Force; "
                    + "ipconfig /release; "
                    + "ipconfig /flushdns\"";

            Runtime.getRuntime().exec(cmdLimpiar).waitFor();
            Thread.sleep(1000);

            // 📶 Conectar WIFI
            System.out.println("📶 [WIFI] Conectando a: [" + redActual + "]");
            String comandoConectar = "netsh wlan connect name=\"" + redActual + "\"";

            Runtime.getRuntime().exec(comandoConectar).waitFor();
            Runtime.getRuntime().exec("ipconfig /renew").waitFor();

            System.out.println("⏳ [SISTEMA] Esperando que Proton libere sesión...");
            Thread.sleep(6000);

            // 🔥 VALIDACIÓN DE RUTA
            File executable = new File(rutaLauncher);

            if (executable.exists()) {

                System.out.println("🛡️ [VPN] Conectando en segundo plano (sin abrir ventana)...");

                // 🔥🔥🔥 AQUÍ ESTÁ LA MAGIA 🔥🔥🔥
                String cmdAbrir = "powershell.exe -Command \""
                        + "Start-Process '" + rutaLauncher + "' "
                        + "-ArgumentList '--connect-fastest' "
                        + "-WindowStyle Hidden\"";

                Runtime.getRuntime().exec(cmdAbrir);

                // ⏳ Espera estabilización VPN
                Thread.sleep(9000);

            } else {
                System.err.println("❌ Error: No se encontró el Launcher en la ruta especificada.");
            }

            System.out.println("✅ [SISTEMA] Proceso completado.");

        } catch (Exception e) {
            System.err.println("❌ Error en el proceso: " + e.getMessage());
        }
    }
}