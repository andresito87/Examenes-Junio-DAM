package hotelpelihilosalamar;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Clase que permite cifrar y descifrar el contenido de un archivo dia.txt
 *
 * @author ANDRÉS SAMUEL PODADERA GONZÁLEZ
 */
public class Cifrador {

    /**
     * Método que permite guardar la cantidad de reservas en el archivo del dia
     * correspondiente
     *
     * @param dia
     * @param cantidad
     */
    public static void guardarReservas(String dia, int cantidad) {
        FileOutputStream fos = null;
        try {

            Cipher cipher = Cipher.getInstance("AES");
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec("12345678".toCharArray(), "SuperMegaSalt".getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey clave = new SecretKeySpec(tmp.getEncoded(), "AES");

            // seleccionamos el modo de desencriptado
            cipher.init(Cipher.DECRYPT_MODE, clave);

            // leer el fichero cifrado, y si no existe, crearlo
            byte[] cantidadReservas;
            Path path = Paths.get(dia + ".txt");
            if (Files.exists(path)) {
                // Si el archivo existe, leer su contenido
                cantidadReservas = Files.readAllBytes(path);
            } else {
                // Si el archivo no existe, crear el archivo vacío
                Files.createFile(path);
                cantidadReservas = new byte[0];
            }
            // se cifran
            byte[] cantidadDescifrada = cipher.doFinal(cantidadReservas);

            // añadimos el texto que queremos almacenar
            int nuevaCantidad = (new String(cantidadDescifrada).equals(""))
                    ? cantidad
                    : Integer.parseInt(new String(cantidadDescifrada)) + cantidad;
            System.out.println("La nueva cantidad de reservas para el " + dia + " son: " + nuevaCantidad);

            // seleccionamos el modo de encriptado
            cipher.init(Cipher.ENCRYPT_MODE, clave);
            // se vuelven a encriptar
            byte[] datosRecifrados = cipher.doFinal((nuevaCantidad + "").getBytes());

            // se almacenan en el archivo correspondiente
            fos = new FileOutputStream(dia + ".txt");
            fos.write(datosRecifrados);

        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | InvalidKeyException
                | IOException
                | IllegalBlockSizeException
                | BadPaddingException
                | InvalidKeySpecException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}
