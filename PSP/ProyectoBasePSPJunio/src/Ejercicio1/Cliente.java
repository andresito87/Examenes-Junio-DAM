package Ejercicio1;

/**
 * Clase que representa un cliente de nuestra tienda, en este caso surfistas
 *
 * @author Andrés Samuel Podadera González
 */
public class Cliente implements Runnable {

    private final String nombre;
    private final Tienda tienda;

    public Cliente(String nombre, Tienda tienda) {
        this.nombre = nombre;
        this.tienda = tienda;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public void run() {
        try {
            tienda.alquilarTabla(this.nombre);
            // tiempo aleatorio entre 10 y 20 segundos
            int tiempoRandom = (int) (Math.random() * 11 + 10);
            Thread.sleep(tiempoRandom * 1000);
            tienda.devolverTabla(this.nombre);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
