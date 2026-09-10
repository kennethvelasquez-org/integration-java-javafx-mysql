/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.kennethvelasquez.system.utils;

import at.favre.lib.crypto.bcrypt.BCrypt;
import java.nio.charset.StandardCharsets;
/**
 * Clase utilitaria para el manejo de hash y verificación de contraseñas usando BCrypt.
 * <p>
 * Implementa la biblioteca criptográfica {@code at.favre.lib.crypto.bcrypt}, permitiendo:
 * <ul>
 *   <li>Generar hashes seguros con sal (*salt*) aleatoria integrada automáticamente.</li>
 *   <li>Configurar versiones del algoritmo (por ejemplo, {@code $2a$}) y factores de costo (*cost factor*).</li>
 *   <li>Verificar contraseñas ingresadas en texto plano contra hashes almacenados en la base de datos de forma resistente a ataques de temporización (*timing attacks*).</li>
 * </ul>
 * </p>
 *
 * @author STEPHRYS
 * @version 1.0
 * @see at.favre.lib.crypto.bcrypt.BCrypt
 */
public class ToolBCrypt {

    /**
     * Factor de costo por defecto utilizado para las operaciones de hash.
     * <p>
     * Determina el número de iteraciones del algoritmo mediante \(2^{\text{COSTO}}\).
     * Un costo de 6 equivale a \(2^6 = 64\) rondas (ideal para pruebas y entornos rápidos).
     * En producción se sugiere un valor entre 10 y 12.
     * </p>
     */
    private static final int DEFAULT_COST = 6;

    /**
     * Constructor por defecto de la clase utilitaria.
     */
    public ToolBCrypt(){
        
    }
    
    /**
     * Método didáctico y de pruebas para explorar el funcionamiento interno de BCrypt.
     * <p>
     * Demuestra cómo BCrypt genera hashes a bajo nivel tanto en formato de bytes ({@code byte[]})
     * como en formato de caracteres ({@code char[]}), y cómo se reconstruye la cadena resultante
     * compuesta por: versión, costo, sal de 16 bytes (en base64) y el hash final.
     * </p>
     *
     * @param password Contraseña de prueba a procesar.
     */
    public void encryptTest(String password) {
        // -------------------------------------------------------------------------
        // 1. GENERACIÓN DE HASH EN FORMATO DE BYTES (byte[])
        // -------------------------------------------------------------------------
        // - BCrypt.with(Version.VERSION_2A): Selecciona la especificación $2a$, estándar
        //   y ampliamente compatible en sistemas UNIX, Spring Security y PHP.
        // - hash(6, bytes): Aplica un factor de costo de 6 (2^6 = 64 iteraciones).
        // - password.getBytes(StandardCharsets.UTF_8): Convierte la clave a bytes en UTF-8.
        byte[] passByte = BCrypt.with(BCrypt.Version.VERSION_2A)
                                .hash(DEFAULT_COST, password.getBytes(StandardCharsets.UTF_8));

        // -------------------------------------------------------------------------
        // 2. GENERACIÓN DE HASH EN FORMATO DE CARACTERES (char[])
        // -------------------------------------------------------------------------
        // Trabajar con char[] es la mejor práctica de seguridad en Java porque los arreglos
        // pueden sobrescribirse en memoria para borrarlos inmediatamente, a diferencia
        // de un String inmutable que permanece en el String Pool hasta que pasa el Garbage Collector.
        char[] passByt2e = BCrypt.with(BCrypt.Version.VERSION_2A)
                                 .hashToChar(DEFAULT_COST, password.toCharArray());

        // -------------------------------------------------------------------------
        // 3. CONVERSIÓN Y RECONSTRUCCIÓN MANUAL DE LOS RESULTADOS
        // -------------------------------------------------------------------------
        // Reconstrucción del arreglo de bytes a String (carácter por carácter)
        StringBuilder resultBuilder = new StringBuilder();
        for (byte byteSelect : passByte) {
            char let = (char) byteSelect;
            resultBuilder.append(let);
        }
        String result = resultBuilder.toString();

        // Reconstrucción del arreglo de caracteres a String
        StringBuilder resultBuilder2 = new StringBuilder();
        for (char characterSelect : passByt2e) {
            resultBuilder2.append(characterSelect);
        }
        String result2 = resultBuilder2.toString();

        // -------------------------------------------------------------------------
        // 4. VISUALIZACIÓN EN CONSOLA
        // -------------------------------------------------------------------------
        // Ambos resultados tienen exactamente 60 caracteres y la estructura:
        // $[versión]$[costo]$[22 caracteres de sal][31 caracteres de hash]
        System.out.println("=== TEST BCRYPT: HASH DESDE BYTES ===");
        System.out.println(result);
        System.out.println("=== TEST BCRYPT: HASH DESDE CHARS ===");
        System.out.println(result2);
    }
    
    /**
     * Genera un hash BCrypt seguro en formato {@link String} listo para ser guardado en la base de datos.
     * <p>
     * Utiliza la configuración por defecto de BCrypt (versión {@code $2a$}, generador seguro de sal aleatoria)
     * y un factor de costo de 6. El resultado es una cadena de exactamente 60 caracteres que incluye la versión,
     * el costo, la sal generada y el hash calculado.
     * </p>
     *
     * @param password Contraseña en texto plano a encriptar.
     * @return Cadena de 60 caracteres con el hash BCrypt completo para almacenar en la base de datos.
     */
    public String encryptToString(String password){
        String hashString = BCrypt.withDefaults().hashToString(DEFAULT_COST, password.toCharArray());
        return hashString;
    }
    
    /**
     * Valida si una contraseña en texto plano coincide con un hash BCrypt previamente almacenado.
     * <p>
     * El verificador extrae automáticamente la sal y el factor de costo contenidos dentro del mismo
     * {@code hashed}, recalcula el hash con esos mismos parámetros y realiza una comparación en tiempo
     * constante para prevenir ataques de temporización (*side-channel timing attacks*).
     * </p>
     *
     * @param password Contraseña ingresada por el usuario en texto plano.
     * @param hashed   Hash BCrypt previamente almacenado en la base de datos.
     * @return {@code true} si la contraseña coincide con el hash; {@code false} si es incorrecta o el formato es inválido.
     */
    public Boolean validatePassword(String password, String hashed){
        BCrypt.Result valid = BCrypt.verifyer().verify(password.toCharArray(), hashed.toCharArray());
        return valid.verified;
    }
    
}
