package proyectobasehotel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class GestorFichero {

    private static final String CLAVE = "1234567890123456";
    private static final String ALGORITMO = "AES";
    private static final Object objLunes = new Object();
    private static final Object objMartes = new Object();
    private static final Object objMiercoles = new Object();
    private static final Object objJueves = new Object();
    private static final Object objViernes = new Object();
    private static final Object objSabado = new Object();
    private static final Object objDomingo = new Object();

    private static byte[] cifrar(String datos) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        SecretKeySpec keySpec = new SecretKeySpec(CLAVE.getBytes(), ALGORITMO);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(datos.getBytes());
    }

    private static String descifrar(byte[] datosCifrados) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITMO);
        SecretKeySpec keySpec = new SecretKeySpec(CLAVE.getBytes(), ALGORITMO);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return new String(cipher.doFinal(datosCifrados));
    }

    public static void actualizarFichero(String dia, int habitaciones) {
        Object lock;
        lock = switch (dia) {
            case "lunes" -> objLunes;
            case "martes" -> objMartes;
            case "miercoles" -> objMiercoles;
            case "jueves" -> objJueves;
            case "viernes" -> objViernes;
            case "sabado" -> objSabado;
            default -> objDomingo;
        };
        try {
            synchronized (lock) {
                Path path = Paths.get(dia + ".txt");
                int reservas = 0;

                // Verificamos si el archivo existe y tiene contenido
                if (Files.exists(path) && Files.size(path) > 0) {
                    byte[] contenidoCifrado = Files.readAllBytes(path);
                    reservas = Integer.parseInt(descifrar(contenidoCifrado));
                }

                // Agregamos el nuevo valor
                reservas += habitaciones;
                if (reservas > 0) {
                    System.out.println("El nuevo dato en "+ dia + ".txt es de: " + reservas);
                }

                // Cifrar el contenido antes de guardarlo
                byte[] contenidoCifrado = cifrar(Integer.toString(reservas));

                // Escribe el contenido cifrado en el archivo.
                // Si el archivo no existe, lo crea (StandardOpenOption.CREATE)
                // Si el archivo ya existe, agrega el nuevo contenido al final 
                // sin borrar lo anterior (StandardOpenOption.APPEND)
                Files.write(path, contenidoCifrado, StandardOpenOption.CREATE);

            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
