
package tarea4pspsolucion;

import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author LuisRosillo <>
 */
public class GestionUsuarios {

    private static Semaforo semaforo = new Semaforo();

    /**
     * Método sincronizado para comprobar si existe la dupla usuario:contraseña
     * 
     * @param email
     * @param password
     * @return boolean
     * @throws Exception
     */
    public static synchronized boolean comprobarLogin(String email, String password) throws Exception {

        boolean login = false;
        boolean encontrado = false;

        semaforo.accesoLeer();

        String listado = GestionCifrados.obtenerUsuarios();

        // Si el listado tiene usuarios
        if (!listado.isEmpty()) {
            // Lo recorremos
            String[] lineas = listado.split("\n");
            for (int i = 0; i < lineas.length && !encontrado; i++) {
                String usuario = lineas[i];
                // Lógica que puede cambiar el valor de 'login'
                String[] partes = usuario.split(":");
                // Comprobamos si este usuario coincide
                if (email.equals(partes[0])) {
                    encontrado = true; // Como no puede existir dos correos iguales, salimos del bucle después de
                                       // comprobar la constraseña
                    if (BCrypt.checkpw(password, partes[1])) {
                        login = true;
                    }
                }
            }
        }

        semaforo.lecturaFinalizada();
        return login;

    }

    /**
     * Método sincronizado para escribir la dupla usuario:contraseña
     * 
     * @param email
     * @param password
     * @return boolean
     * @throws Exception
     */
    public static synchronized boolean realizarRegistro(String email, String password) throws Exception {

        semaforo.accesoEscribir();

        String listado = GestionCifrados.obtenerUsuarios();

        String contrasenaHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
        listado += email + ":" + contrasenaHash + "\n";

        // Cifrar el contenido antes de guardarlo
        boolean registro = GestionCifrados.escribirUsuarios(listado);

        semaforo.escrituraFinalizada();

        return registro;
    }

}
