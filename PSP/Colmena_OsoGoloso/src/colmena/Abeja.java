package colmena;

/**
 *
 * @author andres
 */
public class Abeja implements Runnable {

    private String nombre;
    private final Colmena colmena;

    public Abeja(String nombre, Colmena colmena) {
        this.nombre = nombre;
        this.colmena = colmena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void run() {

        while (!colmena.isEsHoraDeCerrarColmena()) {
            colmena.producirMiel();
            int tiempoRandom = (int) (Math.random() * 4) + 2;
            try {
                Thread.sleep(tiempoRandom * 1000);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println(Thread.currentThread().getName()
                    + " trajo néctar. Ve que la colmena está cerrada y se marcha.");

    }

}
