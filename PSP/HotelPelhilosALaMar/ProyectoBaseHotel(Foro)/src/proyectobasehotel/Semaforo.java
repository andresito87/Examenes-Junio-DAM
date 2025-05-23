package proyectobasehotel;

/**
 *
 * @author Alicia
 */
public class Semaforo {
    
    private static int read = 0;
    private static boolean writer = false;

    public synchronized  void onRead() throws InterruptedException {
       
        while (writer) {
            // Esperamos a que termine el escritor
            wait();
        }
        read++; // Registramos que hay un nuevo lector activo

    }

    
    public synchronized void offRead() {

        read--; // Salimos como lector
        if (read == 0) {
            // Si no quedan lectores, avisamos a los escritores
            notifyAll();
        }

    }

    public synchronized void onWriter() throws InterruptedException {

        while (read > 0 || writer) {
            // Esperamos a que no haya lectores ni escritores
            wait();
        }
        writer = true; // Indicamos que estamos escribiendo

    }

    
    public synchronized void offWriter() {

        writer = false; // Ya no estamos escribiendo
        notifyAll(); // Notoficamos a todos los que esperaban
    }
}
