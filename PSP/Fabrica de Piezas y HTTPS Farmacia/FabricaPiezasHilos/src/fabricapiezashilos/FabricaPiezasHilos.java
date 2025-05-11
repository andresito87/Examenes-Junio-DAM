package fabricapiezashilos;

/**
 *
 * @author andres
 */
public class FabricaPiezasHilos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Thread[] hilos = new Thread[4];

        CintaTransportadora cinta = new CintaTransportadora("Cinta AR43", 5);

        // Crear tres hilos fabricadores y un hilo empaquetador
        Fabricadora fabricador1 = new Fabricadora("Hilo fabricador 1", cinta);
        Fabricadora fabricador2 = new Fabricadora("Hilo fabricador 2", cinta);
        Fabricadora fabricador3 = new Fabricadora("Hilo fabricador 3", cinta);
        Empaquetadora empaquetadora = new Empaquetadora("Hilo empaquetador", cinta);

        // Almacenar hilos
        hilos[0] = new Thread(fabricador1);
        hilos[0].setName(fabricador1.getNombre());
        hilos[1] = new Thread(fabricador2);
        hilos[1].setName(fabricador2.getNombre());
        hilos[2] = new Thread(fabricador3);
        hilos[2].setName(fabricador3.getNombre());
        hilos[3] = new Thread(empaquetadora);
        hilos[3].setName(empaquetadora.getNombre());

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

        System.out.println("Todos las máquinas han terminado. Fábrica cerrada.");
    }

}
