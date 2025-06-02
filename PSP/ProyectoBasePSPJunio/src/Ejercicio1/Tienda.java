package Ejercicio1;

/**
 * Clase que representa una tienda de tablas de surf Es el recurso compartido
 *
 * @author Andrés Samuel Podadera González
 */
public class Tienda {

    private final String RESET = "\u001B[0m";
    private final String CYAN = "\u001B[36m";
    private final String YELLOW = "\u001B[33m";
    private final String GREEN = "\u001B[32m";

    private static final int CANTIDAD_TABLAS = 5;
    private static final double PRECIO_ALQUILER_TABLA = 15.00;
    private int tablasDisponibles = 5;
    private int clientesAtendidos = 0;
    private double gananciaTotal = 0.00;
    private final String nombre;

    public Tienda(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public synchronized void alquilarTabla(String nombreCliente) throws InterruptedException {
        while (tablasDisponibles == 0) {
            System.out.println("No hay tablas disponibles, debes esperar " + nombreCliente);
            wait();
        }

        tablasDisponibles--;
        gananciaTotal += PRECIO_ALQUILER_TABLA;
        System.out.println(CYAN + nombreCliente + " ha cogido una tabla. Tablas disponibles: " + tablasDisponibles + RESET);
    }

    public synchronized void devolverTabla(String nombreCliente) {
        tablasDisponibles++;
        clientesAtendidos++;
        System.out.println(YELLOW + nombreCliente + " ha devuelto su tabla. Tablas disponibles: " + tablasDisponibles + RESET);
        notifyAll();
    }

    public synchronized void mostrarEstado() {
        System.out.println(GREEN + "Estado de la tienda " + this.nombre + ":");
        System.out.println(GREEN + "\tClientes totales: " + clientesAtendidos);
        System.out.println(GREEN + "\tDinero ganado: " + gananciaTotal + " €");
        System.out.println(GREEN + "\tTablas de Surf disponibles: " + tablasDisponibles);
        System.out.println(GREEN + "\tTablas alquiladas: " + (CANTIDAD_TABLAS - tablasDisponibles) + RESET);
    }
}
