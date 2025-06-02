package Ejercicio2;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Clase que permite gestionar la lectura y escritura en un archivo de texto de
 * forma concurrente
 *
 * @author Andres Samuel Podadera González
 */
public class GestorFichero {

    private static final String CLAVE = "1234567890123456";
    private static final String ALGORITMO = "AES";
    private static final Object objFichero = new Object();

    /**
     * Permite cifrar un archivo de texto con el algoritmo AES
     *
     * @param datos
     * @return array de bytes con el texto del archivo
     * @throws Exception
     */
    private static byte[] cifrar(String datos) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        SecretKeySpec keySpec = new SecretKeySpec(CLAVE.getBytes(), ALGORITMO);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(datos.getBytes());
    }

    /**
     * Método que permite descifrar un array de bytes encriptado con el
     * algortimo AES
     *
     * @param datosCifrados
     * @return
     * @throws Exception
     */
    private static String descifrar(byte[] datosCifrados) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        SecretKeySpec keySpec = new SecretKeySpec(CLAVE.getBytes(), ALGORITMO);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return new String(cipher.doFinal(datosCifrados));
    }

    /**
     * Método que permite actualizar el contenido de una archivo de texto
     * gestionando la concurrencia de acceso para lectura y/o escritura
     *
     * @param ganancias
     */
    public static void actualizarFichero(float ganancias) {
        try {
            synchronized (objFichero) {
                Path path = Paths.get("ganancias.txt");
                float ganaciasActualizadas = 0;

                // comprobamos si existe el archivo y recuperamos sus datos
                if (Files.exists(path) && Files.size(path) > 0) {
                    byte[] contenidoCifrado = Files.readAllBytes(path);
                    ganaciasActualizadas = Float.parseFloat(descifrar(contenidoCifrado));
                }

                // actualizamos las ganancias
                ganaciasActualizadas += ganancias;
                System.out.println("La nueva cantidad en ganancias.txt es de : " + ganaciasActualizadas);

                // obtenemos las nuevas ganancias actualizadas
                byte[] contenidoCifrado = cifrar(Float.toString(ganaciasActualizadas));

                // actualizamos el contenido del archivo
                Files.write(path, contenidoCifrado, StandardOpenOption.CREATE);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
