package tarea4pspsolucion;

public class Semaforo {

    public final static int LIBRE = 0;
    public final static int CON_LECTORES = 1;
    public final static int CON_ESCRITOR = 2;
    private static int estado = LIBRE;
    private static int tLectores = 0;

    // Método sincronizado que da acceso a la lectura de datos
    public synchronized void accesoLeer() {
        // método sincronizado. Sólo un hilo lo usa a la vez
        if (estado == LIBRE) { // BD sin lectores ni escritores. Pueden etrar a leer
            estado = CON_LECTORES; // cambia estado, ya hay lector
        } else if (estado != CON_LECTORES) { // si no está libre, ni con lectores
            while (estado == CON_ESCRITOR) {
                try {
                    wait(); // pone en espera al hilo que intenta leer datos
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
            estado = CON_LECTORES; // cambia estado, ya hay lector
        }
        tLectores++; // otro lector más
    }

    // método que da acceso para escribir datos si el estado de la BD lo permite
    public synchronized void accesoEscribir() {
        if (estado == LIBRE) { // sin lectores ni escritores
            estado = CON_ESCRITOR; // cambia estado
        } else {// si no está libre
            while (estado != LIBRE) { // miestras BD está ocupada con lectores, o con un escritor
                try {
                    wait(); // pone en espera al hilo que intenta escribir datos
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            } 
            // el estado ahora es LIBRE
            estado = CON_ESCRITOR; // cambia estado
        }
    }

    public synchronized void escrituraFinalizada() {
        estado = LIBRE; // cambia estado
        notify(); // notifica a los hilos en espera que ya ha finalizado
    }

    public synchronized void lecturaFinalizada() {
        tLectores--; // un lector menos leyendo
        if (tLectores == 0) { // no hay lectores en la BD
            estado = LIBRE; // cambia el estado
            notify(); // notifica a los hilos en espera que ya ha finalizado
        }
    }

}
