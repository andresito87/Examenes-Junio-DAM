package servidorhttps_newsletter;

/**
 *
 * @author ANDRÉS SAMUEL PODADERA GONZÁLEZ
 */
public class AlmacenUsuarios {

    private final static int LIBRE = 0;
    private final static int CON_LECTORES = 1;
    private final static int CON_ESCRITOR = 2;

    private int estado = LIBRE;
    private int numLectores = 0;

    /**
     * Se encarga de controlar el acceso concurrente de escritura en el archivo
     *
     * @param datosUSuario
     * @return
     */
    public synchronized String accesoEscribir(String[] datosUSuario) {
        String resultado;
        if (estado == LIBRE) {
            estado = CON_ESCRITOR;
            // desencripta, lee, añade información y vuelve a encriptar
            resultado = this.guardar(datosUSuario);
        } else {
            while (estado != LIBRE) {
                try {
                    // no está libre, el hilo permanece a la espera
                    wait();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
            estado = CON_ESCRITOR;
            // desencripta, lee, añade información y vuelve a encriptar
            resultado = this.guardar(datosUSuario);
        }
        return resultado;
    }

    /**
     * Se utiliza para indicar que la escritura de un hilo ha terminado
     */
    public synchronized void escrituraFinalizada() {
        estado = LIBRE;
        notifyAll();
    }

    /**
     * Se utiliza para indicar que la lectura de un hilo ha terminado
     */
    public synchronized void lecturaFinalizada() {
        numLectores--;
        if (numLectores == 0) {
            estado = LIBRE;
            notifyAll();
        }
    }

    public synchronized String obtenerDatosUsuarios() {

        if (estado == LIBRE) {
            return Cifrador.leerArchivoUsuarios();
        } else if (estado != CON_LECTORES) {
            while (estado == CON_ESCRITOR) {
                try {
                    // el hilo queda en espera
                    wait();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
            estado = CON_LECTORES;
            return Cifrador.leerArchivoUsuarios();
        } else {
            return Cifrador.leerArchivoUsuarios();
        }
    }

    public String guardar(String[] datosUsuario) {

        // Evitar duplicados (opcional)
        if (Cifrador.existeEmail(datosUsuario[1])) {
            return "El usuario ya está registrado.";
        }
        // Añadir la nueva línea al final
        Cifrador.guardar(datosUsuario);
        System.out.println("Hilo guarda la información en newsletter.txt");
        return "Usuario suscrito correctamente";
    }
}
