package colmena;

/**
 *
 * @author andres
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Thread[] hilos = new Thread[3];

        Colmena colmena = new Colmena();

        // Crear tres hilos fabricadores y un hilo empaquetador
        Abeja abeja1 = new Abeja("Abeja 1", colmena);
        Abeja abeja2 = new Abeja("Abeja 2", colmena);
        Oso oso = new Oso(colmena);

        // Almacenar hilos
        hilos[0] = new Thread(abeja1);
        hilos[0].setName(abeja1.getNombre());
        hilos[1] = new Thread(abeja2);
        hilos[1].setName(abeja2.getNombre());
        hilos[2] = new Thread(oso);
        hilos[2].setName(oso.getNombre());

        // Ejecutamos todos los hilos en paralelo
        for (Thread hilo : hilos) {
            hilo.start();
        }

        // Esperar a que todos los hilos terminen
        for (Thread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                System.err.println("Error al esperar el hilo: " + e.getMessage());
            }
        }

        System.out.println("Todos los hilos han acabado. Colmena cerrada.");
        
    }
    
}
