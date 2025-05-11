package fabricapiezashilos;

/**
 *
 * @author andres
 */
public class CintaTransportadora {

    private int capacidad = 5;
    public final int LIMITE_PIEZAS_DIARIAS_FABRICADAS = 10;
    private int piezasEnCinta = 0;
    private int piezasFabricadas = 0;
    private final String nombre;

    public CintaTransportadora(String nombre, int capacidad) {
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPiezasEnCinta() {
        return piezasEnCinta;
    }

    public void setPiezasEnCinta(int piezasEnCinta) {
        this.piezasEnCinta = piezasEnCinta;
    }

    public int getPiezasFabricadas() {
        return piezasFabricadas;
    }

    public void setPiezasFabricadas(int piezasFabricadas) {
        this.piezasFabricadas = piezasFabricadas;
    }

    public synchronized void colocarPieza() throws InterruptedException {
        while (this.piezasEnCinta >= this.capacidad) { // si la cinta esta llena, esperan
            wait();
        }
        if (this.piezasFabricadas < LIMITE_PIEZAS_DIARIAS_FABRICADAS) {

            // colocamos una pieza
            this.piezasEnCinta += 1;
            this.piezasFabricadas += 1;

        }

        // Notificar a todos que hay piezas disponibles
        notifyAll();
    }

    public synchronized void sacarPieza() throws InterruptedException {

        while (this.piezasEnCinta == 0) { // si la cinta está vacia, esperar
            wait();
        }

        // Sacamos una pieza de la cinta
        piezasEnCinta -= 1;

        // Notificar a fabricadores que hay espacio en la cinta para más piezas
        notifyAll();
    }

    public void mostrarEstado() {
        System.out.println(Thread.currentThread().getName() + " | Cantidad de piezas en cinta:" + this.piezasEnCinta
                + " | Piezas fabricadas: " + this.piezasFabricadas);
    }

}
