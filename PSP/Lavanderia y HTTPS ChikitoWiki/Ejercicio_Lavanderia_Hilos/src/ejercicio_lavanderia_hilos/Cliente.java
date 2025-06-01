package ejercicio_lavanderia_hilos;

public class Cliente implements Runnable {

    private final String nombre;
    private final Lavanderia lavanderia;

    public Cliente(String nombre, Lavanderia lavanderia) {
        this.nombre = nombre;
        this.lavanderia = lavanderia;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public void run() {
        try {
            lavanderia.lavarRopa(this.nombre);
            int tiempoRandom = (int) (Math.random() * 11 + 5);
            Thread.sleep(tiempoRandom * 1000);
            lavanderia.terminarLavado(this.nombre);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
