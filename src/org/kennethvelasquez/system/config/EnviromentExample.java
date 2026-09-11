/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.config;

/**
 * Clase de configuración del entorno para la conexión a la base de datos.
 * <p>
 * Esta clase centraliza las constantes estáticas necesarias para establecer 
 * una conexión JDBC. Al ser atributos {@code static final}, pertenecen a la 
 * clase en sí misma y no a instancias individuales, lo que garantiza un único 
 * punto de verdad para la configuración de la base de datos en toda la aplicación.
 * </p>
 * 
 * <h2>Ejemplo de construcción de la conexión JDBC:</h2>
 * <pre>{@code
 * // 1. Construir la URL completa concatenando el protocolo, el servicio y la base de datos
 * String jdbcUrl = "jdbc:mysql://" + EnviromentExample.LOCATION_SERVICE + "/" + EnviromentExample.DATABASE;
 * 
 * // 2. Establecer la conexión (Orden: url, usuario, contraseña)
 * try (Connection conn = DriverManager.getConnection(jdbcUrl, EnviromentExample.USER, EnviromentExample.PASSWORD)) {
 *     System.out.println("Conexión establecida exitosamente con: " + jdbcUrl);
 * } catch (SQLException e) {
 *     System.err.println("Error al conectar a la base de datos: " + e.getMessage());
 * }
 * }</pre>
 *
 * @author Tu Nombre o Equipo de Desarrollo
 * @version 1.1
 * @since 2026
 * @see java.sql.DriverManager
 * @see java.sql.Connection
 */
public class EnviromentExample {

    /**
     * Nombre de usuario para la autenticación en la base de datos.
     * <p>
     * Constante de clase utilizada como segundo parámetro en 
     * {@link java.sql.DriverManager#getConnection(String, String, String)}.
     * </p>
     */
    protected static final String USER = "root";

    /**
     * Contraseña de acceso para el usuario de la base de datos.
     * <p>
     * Constante de clase utilizada como tercer parámetro en 
     * {@link java.sql.DriverManager#getConnection(String, String, String)}.
     * </p>
     * <b>⚠️ Nota de seguridad:</b> En entornos de producción, se recomienda 
     * encarecidamente no hardcodear esta contraseña. Considere usar variables 
     * de entorno ({@code System.getenv("DB_PASSWORD")}) o archivos de propiedades externos.
     */
    protected static final String PASSWORD = "tu_contraseña_segura";

    /**
     * Nombre del esquema o base de datos a la que se desea conectar.
     * <p>
     * Este valor se concatena al final de la URL del servicio para apuntar 
     * a la base de datos específica (ej: {@code .../mi_base_de_datos}).
     * </p>
     */
    protected static final String DATABASE = "base_de_datos";

    /**
     * Dirección IP o nombre del host del servidor de base de datos, junto con su puerto.
     * <p>
     * No incluye el protocolo JDBC. Ejemplos de valores válidos:
     * <ul>
     *   <li>{@code "localhost:3306"} (Desarrollo local)</li>
     *   <li>{@code "192.168.1.50:3306"} (Servidor en red local)</li>
     *   <li>{@code "db.miempresa.com:3306"} (Servidor en la nube)</li>
     * </ul>
     * Se concatena con el prefijo {@code "jdbc:mysql://"} para formar la URL completa.
     * </p>
     */
    protected static final String LOCATION_SERVICE = "localhost:3306";
}