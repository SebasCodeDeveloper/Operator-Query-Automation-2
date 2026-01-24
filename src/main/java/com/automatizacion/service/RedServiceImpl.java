package com.automatizacion.service;

/**
 * Implementación del servicio de red mediante comandos nativos de Windows.
 * Utiliza 'netsh' para alternar entre puntos de acceso Wi-Fi configurados
 * con el fin de refrescar la dirección IP y evitar bloqueos.
 * * @author SebasCodeDev
 * @version 1.3.1
 * @since 01/24/2026
 */
public class RedServiceImpl implements RedService {

    // Identificador del punto de acceso actual
    private String redActual = " claro ";

    /**
     * Alterna la conexión entre dos perfiles Wi-Fi específicos.
     * Ejecuta comandos de consola (cmd) para gestionar la interfaz inalámbrica.
     */
    @Override
    public void alternarRed() {
        // Lógica de alternancia (Toggle) entre redes disponibles
        if (redActual.equals(" claro ")) {
            redActual = " claro  2";
        } else {
            redActual = " claro ";
        }

        try {
            System.out.println("📶 [WIFI] Cambiando a: [" + redActual + "]");

            // Desconexión de la interfaz actual para forzar la liberación de IP
            Process p1 = Runtime.getRuntime()
                    .exec("netsh wlan disconnect interface=\"Wi-Fi\"");
            p1.waitFor();
            Thread.sleep(2000);

            // Comando para conectar al nuevo perfil Wi-Fi especificado
            String[] cmdConectar = {
                    "cmd.exe",
                    "/c",
                    "netsh wlan connect name=\"" + redActual + "\" interface=\"Wi-Fi\""
            };

            Process p2 = Runtime.getRuntime().exec(cmdConectar);
            p2.waitFor();

            // Tiempo de espera técnico para asegurar la obtención de una nueva IP dinámica
            System.out.println("⏳ [WIFI] Esperando 2s para estabilizar conexión...");
            Thread.sleep(2000);

        } catch (Exception e) {
            System.err.println("❌ Error en cambio de red: " + e.getMessage());
        }
    }
}