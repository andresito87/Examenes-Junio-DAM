package colmena;

/**
 *
 * @author andres
 */
public class Colmena {

    public final int CAPACIDAD_MAX = 30;
    public final int MINIMA_CANTIDAD_MIEL_A_CONSUMIR = 3;
    private int mielProducida = 0;
    private int mielActual = 0;
    private int comilonas = 0;
    private boolean esHoraDeCerrarColmena = false;
    private int mielEnPanza = 0;

    public Colmena() {
    }

    public int getMielProducida() {
        return mielProducida;
    }

    public void setMielProducida(int mielProducida) {
        this.mielProducida = mielProducida;
    }

    public int getMielActual() {
        return mielActual;
    }

    public void setMielActual(int mielActual) {
        this.mielActual = mielActual;
    }

    public int getComilonas() {
        return comilonas;
    }

    public void setComilonas(int comilonas) {
        this.comilonas = comilonas;
    }

    public boolean isEsHoraDeCerrarColmena() {
        return esHoraDeCerrarColmena;
    }

    public void setEsHoraDeCerrarColmena(boolean esHoraDeCerrarColmena) {
        this.esHoraDeCerrarColmena = esHoraDeCerrarColmena;
    }

    public synchronized void producirMiel() {
        while (this.getMielActual() >= CAPACIDAD_MAX) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        if (!this.esHoraDeCerrarColmena) {
            this.mielProducida++;
            this.mielActual++;
            System.out.println(Thread.currentThread().getName()
                    + " trajo nectar. Cantidad de miel actual: " + this.mielActual);
            notifyAll();
        } else {
            System.out.println(Thread.currentThread().getName()
                    + " trajo néctar. Ve que la colmena está cerrada y se marcha.");
        }

    }

    public synchronized void consumirMiel() {
        while (this.getMielActual() < MINIMA_CANTIDAD_MIEL_A_CONSUMIR) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        this.comilonas++;
        this.mielEnPanza += this.mielActual;
        System.out.println("Oso: Comilona nº " + this.comilonas
                + ". Miel consumida: " + this.mielActual
                + ". Miel en la panza: " + this.mielEnPanza);
        this.mielActual = 0;
        if (comilonas >= 5) {
            this.esHoraDeCerrarColmena = true;
            System.out.println("El oso está lleno. ¡Hora de hibernar!");
            notifyAll();
        }
    }

}
