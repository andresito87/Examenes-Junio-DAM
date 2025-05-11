package fabricapiezashilos;

/**
 *
 * @author andres
 */
public class Fabricadora implements Runnable {

    private final String nombre;
    private CintaTransportadora cinta;

    public Fabricadora(String nombre, CintaTransportadora cinta) {
        this.nombre = nombre;
        this.cinta = cinta;
    }

    public String getNombre() {
        return nombre;
    }

    public CintaTransportadora getCinta() {
        return cinta;
    }

    public void setCinta(CintaTransportadora cinta) {
        this.cinta = cinta;
    }

    @Override
    public void run() {
        while (cinta.getPiezasFabricadas() < cinta.LIMITE_PIEZAS_DIARIAS_FABRICADAS) {
            try {
                cinta.colocarPieza();
                cinta.mostrarEstado();
                // El hilo duerme durante un tiempo aleatorio entre 1 y 3 segundos
                int tiempoRandom = (int) (Math.random() * 4 + 1);
                Thread.sleep(tiempoRandom * 1000);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
