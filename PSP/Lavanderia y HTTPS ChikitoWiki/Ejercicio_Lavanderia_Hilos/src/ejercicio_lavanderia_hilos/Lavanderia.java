package ejercicio_lavanderia_hilos;

public class Lavanderia {

    private final String RESET = "\u001B[0m";
    private final String CYAN = "\u001B[36m";
    private final String YELLOW = "\u001B[33m";
    private final String GREEN = "\u001B[32m";

    private static final int CANTIDAD_LAVADORAS = 4;
    private static final double PRECIO_LAVADO = 3.00;
    private int lavadorasLibres = 4;
    private int clientesAtendidos = 0;
    private double gananciaTotal = 0.00;

    public Lavanderia() {
    }

    public synchronized void lavarRopa(String nombreCliente) throws InterruptedException {
        while (lavadorasLibres == 0) {
            wait();
        }

        lavadorasLibres--;
        gananciaTotal += PRECIO_LAVADO;
        System.out.println("\n" + CYAN + nombreCliente + " ha empezado a lavar su ropa." + RESET);
    }

    public synchronized void terminarLavado(String nombreCliente) {
        System.out.println("\n" + YELLOW + nombreCliente + " ha acabado de lavar su ropa" + RESET);
        lavadorasLibres++;
        clientesAtendidos++;
        notifyAll();
    }

    public synchronized void mostrarEstado() {
        System.out.println(GREEN + "****** Estado de la Lavandería ******");
        System.out.println(GREEN + "Clientes atendidos: " + clientesAtendidos);
        System.out.println(GREEN + "Ganancia total: " + gananciaTotal + " €");
        System.out.println(GREEN + "Lavadoras disponibles: " + lavadorasLibres);
        System.out.println(GREEN + "Lavadoras en uso: " + (CANTIDAD_LAVADORAS - lavadorasLibres) + RESET);
    }
}
