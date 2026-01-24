package com.automatizacion.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Gestor de configuraciones encargado de cargar y administrar las propiedades
 * externas del proyecto desde el archivo {@code config.properties}.
 * * Permite centralizar valores como URLs, rutas de archivos y parámetros
 * de ejecución sin necesidad de modificar el código fuente.
 *
 * @author SebasCodeDev
 * @version 1.3.1
 * @since 01/24/2026
 */
public class ConfigManager {

    /**
     * Objeto contenedor de las duplas clave-valor del archivo de configuración.
     */
    private static final Properties properties = new Properties();

    // Bloque estático para asegurar que las propiedades se carguen al iniciar la aplicación
    static {
        try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties")) {

            if (input == null) {
                System.err.println("⚠ No se encontró el archivo config.properties dentro de resources.");
            } else {
                properties.load(input);
            }

        } catch (IOException e) {
            System.err.println("⚠ Error cargando config.properties: " + e.getMessage());
        }
    }

    /**
     * Recupera un valor específico basado en su identificador único (key).
     *
     * @param key Identificador de la propiedad definida en el archivo .properties.
     * @return El valor asociado como {@code String}, o {@code null} si la clave no existe.
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }
}