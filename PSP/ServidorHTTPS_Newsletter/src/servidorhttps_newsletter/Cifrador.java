package servidorhttps_newsletter;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
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
 * Se utiliza para las tareas de encriptado y desencriptado de archivos
 *
 * @author ANDRÉS SAMUEL PODADERA GONZÁLEZ
 */
public class Cifrador {

    /**
     * Método que permite guardar usuario:contraseña en el archivo
     * newsletter.txt
     *
     * @param datosUsuario
     */
    public static void guardar(String[] datosUsuario) {
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
            byte[] datosCifrados;
            Path path = Paths.get("newsletter.txt");
            if (Files.exists(path)) {
                // Si el archivo existe, leer su contenido
                datosCifrados = Files.readAllBytes(path);
            } else {
                // Si el archivo no existe, crear el archivo vacío
                Files.createFile(path);
                datosCifrados = new byte[0];
            }
            // se cifran
            byte[] datosDescifrados = cipher.doFinal(datosCifrados);

            // añadimos el texto que queremos almacenar
            String usuarios = new String(datosDescifrados) + (datosUsuario[0] + ":" + datosUsuario[1] + "\n");

            // seleccionamos el modo de encriptado
            cipher.init(Cipher.ENCRYPT_MODE, clave);
            // se vuelven a encriptar
            byte[] datosRecifrados = cipher.doFinal(usuarios.getBytes());

            // se almacenan en el archivo correspondiente
            fos = new FileOutputStream("newsletter.txt");
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

    /**
     * Método que permite leer el contenido del archivo encriptado
     * newsletter.txt
     *
     * @return
     */
    public static String leerArchivoUsuarios() {
        String resultado = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec("12345678".toCharArray(), "SuperMegaSalt".getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey clave = new SecretKeySpec(tmp.getEncoded(), "AES");

            // seleccionamos el modo de desencriptado
            cipher.init(Cipher.DECRYPT_MODE, clave);

            if (Files.exists(Paths.get("newsletter.txt"))) {

                //leemos el fichero cifrado
                byte[] datosCifrados = Files.readAllBytes(Paths.get("newsletter.txt"));
                // se cifran
                byte[] datosDescifrados = cipher.doFinal(datosCifrados);

                resultado = new String(datosDescifrados);
            } else {
                resultado = "";
            }

        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | InvalidKeyException
                | IOException
                | IllegalBlockSizeException
                | BadPaddingException
                | InvalidKeySpecException ex) {
            System.out.println(ex.getMessage());
        }

        return resultado;
    }

    /**
     * Permite comprobar si el email del usuario existe ya en el archivo, si el
     * usuario ya está suscrito
     *
     * @param email
     * @return
     */
    public static boolean existeEmail(String email) {
        String usuarios = leerArchivoUsuarios();
        if (usuarios != null) {
            try (BufferedReader br = new BufferedReader(new StringReader(usuarios))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(":");
                    if (partes.length == 2 && partes[1].equals(email)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                System.err.println("Error al leer el archivo de usuarios: " + e.getMessage());
            }
        }
        return false; // email no encontrado
    }

}
