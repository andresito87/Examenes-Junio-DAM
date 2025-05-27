package colmena;

/**
 *
 * @author andres
 */
public class Oso implements Runnable {

    private final String nombre;
    private final Colmena colmena;

    public Oso(Colmena colmena) {
        this.nombre = "Oso";
        this.colmena = colmena;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public void run() {

        while (!colmena.isEsHoraDeCerrarColmena()) {
            colmena.consumirMiel();
            int tiempoRandom = (int) (Math.random() * 14) + 2;
            try {
                Thread.sleep(tiempoRandom * 1000);
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
