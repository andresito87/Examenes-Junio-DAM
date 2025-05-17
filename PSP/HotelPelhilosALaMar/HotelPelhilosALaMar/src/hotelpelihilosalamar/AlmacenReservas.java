package hotelpelihilosalamar;

/**
 * Representa el recurso compartido. Se encarga de gestionar el acceso
 * concurrente al archivo del dia correspondiente
 *
 * @author ANDRÉS SAMUEL PODADERA GONZÁLEZ
 */
public class AlmacenReservas {

    public final static int LIBRE = 0;
    public final static int CON_ESCRITOR = 1;

    private int estado = LIBRE;

    /**
     * Se encarga de controlar el acceso concurrente de escritura en el archivo
     *
     * @param dia
     * @param cantidad
     */
    public synchronized void accesoEscribir(String dia, int cantidad) {
        if (estado == LIBRE) {
            estado = CON_ESCRITOR;
            // desencripta, lee, añade información y vuelve a encriptar
            Cifrador.guardarReservas(dia, cantidad);
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
            Cifrador.guardarReservas(dia, cantidad);
        }
        System.out.println("Hilo guarda la información en " + dia + ".txt");
    }

    /**
     * Se utiliza para indicar que la escritura de un hilo ha terminado
     */
    public synchronized void escrituraFinalizada() {
        estado = LIBRE;
        notifyAll();
    }
}
