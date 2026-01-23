package com.automatizacion.service;

public class RedServiceImpl implements RedService {

    private String redActual = " claro ";

    @Override
    public void alternarRed() {
        if (redActual.equals(" claro ")) {
            redActual = " claro  2";
        } else {
            redActual = " claro ";
        }

        try {
            System.out.println("📶 [WIFI] Cambiando a: [" + redActual + "]");

            Process p1 = Runtime.getRuntime()
                    .exec("netsh wlan disconnect interface=\"Wi-Fi\"");
            p1.waitFor();
            Thread.sleep(2000);

            String[] cmdConectar = {
                    "cmd.exe",
                    "/c",
                    "netsh wlan connect name=\"" + redActual + "\" interface=\"Wi-Fi\""
            };

            Process p2 = Runtime.getRuntime().exec(cmdConectar);
            p2.waitFor();

            System.out.println("⏳ [WIFI] Esperando 2s para estabilizar conexión...");
            Thread.sleep(2000);

        } catch (Exception e) {
            System.err.println("❌ Error en cambio de red: " + e.getMessage());
        }
    }
}
