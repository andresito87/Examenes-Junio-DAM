package fabricapiezashilos;

/**
 *
 * @author andres
 */
public class Empaquetadora implements Runnable {

    private final String nombre;
    private CintaTransportadora cinta;

    public Empaquetadora(String nombre, CintaTransportadora cinta) {
        this.nombre = nombre;
        this.cinta = cinta;
    }

    public String getNombre() {
        return nombre;
    }

    public CintaTransportadora getCinta() {
        return cinta;
    }

    @Override
    public void run() {
        while (cinta.getPiezasFabricadas() < cinta.LIMITE_PIEZAS_DIARIAS_FABRICADAS // en este caso no es necesaria porque hay 3 maquinas productoras
                || cinta.getPiezasEnCinta() > 0) {
            try {
                cinta.sacarPieza();
                cinta.mostrarEstado();
                Thread.sleep(2000); // Simular espera de 2 segundos
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
