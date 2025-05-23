package proyectobasehotel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Alicia
 */
public class Utilidades {

    private static final String ALGORITMO = "AES"; // algoritmo de cifrado
    private static final byte[] CLAVE_AES = "1234567890123456".getBytes(); // Clave AES de 16 bytes

    private static final Semaforo semaforo = new Semaforo(); // Control de concurrencia

    private static byte[] cifrar(String datos) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        SecretKey clave = new SecretKeySpec(CLAVE_AES, ALGORITMO);
        cipher.init(Cipher.ENCRYPT_MODE, clave);
        return cipher.doFinal(datos.getBytes());
    }

    private static String descifrar(byte[] datosCifrados) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        SecretKey clave = new SecretKeySpec(CLAVE_AES, ALGORITMO);
        cipher.init(Cipher.DECRYPT_MODE, clave);
        return new String(cipher.doFinal(datosCifrados));
    }

    private static void escribirArchivoCifrado(String file, int cantidad) throws Exception {
        try {
            semaforo.onWriter(); // Bloqueo de escritura
            byte[] datosCifrados = cifrar(String.valueOf(cantidad));
            Files.write(Paths.get(file), datosCifrados);
            semaforo.offWriter(); // Desbloqueo de escritura
        } catch (IOException e) {
            System.out.println("Error al cifrar y escribir el archivo: " + e.getMessage());
        }
    }

    private static String leerArchivoDescifrado(String file) throws Exception {
        String resultado = "";

        try {
            if (Files.exists(Paths.get(file))) {
                byte[] datosCifrados = Files.readAllBytes(Paths.get(file));
                semaforo.onRead();
                resultado = descifrar(datosCifrados);
                semaforo.offRead();
            }
        } catch (IOException e) {
            System.out.println("Error al descifrar el archivo: " + e.getMessage());
        }

        return resultado;
    }

    public static boolean registrarReserva(String dia, int cantidad) {
        String archivo = dia + ".txt";
        int reservas;
        try {
            if (Files.size(Paths.get(archivo)) > 0) {    // si el dia ya tiene reservas

                reservas = Integer.parseInt(leerArchivoDescifrado(archivo));
                reservas += cantidad;
                escribirArchivoCifrado(archivo, reservas);

                System.out.println("Fichero: " + archivo + " - Total reservas:" + reservas);

            } else {
                escribirArchivoCifrado(archivo, cantidad);
                
                System.out.println("Fichero: " + archivo + " - Total reservas:" + cantidad);
            }

            return true;
        } catch (Exception ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

}
