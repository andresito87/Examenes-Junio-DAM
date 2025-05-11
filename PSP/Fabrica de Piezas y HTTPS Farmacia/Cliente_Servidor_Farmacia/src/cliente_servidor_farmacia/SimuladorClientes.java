package cliente_servidor_farmacia;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author andres
 */
public class SimuladorClientes {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<Thread> hilos = new ArrayList<>();
        int opcion = 0;

        while (opcion != 2) {
            System.out.println("\nMenú:");
            System.out.println("1. Simular peticiones clientes");
            System.out.println("2. Terminar programa");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1 -> {
                    System.out.print("¿Cuántos clientes quieres simular?: ");
                    int cantidadClientes = scanner.nextInt();
                    
                    for (int i = 0; i < cantidadClientes; i++) {
                        int productoRandom = (int) (Math.random() * 3 + 1);

                        int cantidadRandom;
                        switch (productoRandom) {

                            case 1 -> {
                                cantidadRandom = (int) (Math.random() * 3 + 1);
                                hilos.add(new Cliente("Hilo " + (i + 1), "Paracetamol", cantidadRandom));
                            }
                            case 2 -> {
                                cantidadRandom = (int) (Math.random() * 3 + 1);
                                hilos.add(new Cliente("Hilo " + (i + 1), "Ibuprofeno", cantidadRandom));
                            }
                            case 3 -> {
                                cantidadRandom = (int) (Math.random() * 3 + 1);
                                hilos.add(new Cliente("Hilo " + (i + 1), "Vitamina C", cantidadRandom));
                            }
                            default ->
                                throw new AssertionError();
                        }
                    }   // Ejecutamos todos los hilos de forma concurrente
                    for (Thread hilo : hilos) {
                        hilo.start();
                    }   // Esperar a que todos los hilos terminen
                    for (Thread hilo : hilos) {
                        try {
                            hilo.join();
                        } catch (InterruptedException e) {
                            System.err.println("Error al esperar el hilo: " + e.getMessage());
                        }
                    }
                    System.out.println("\nTodos los clientes han terminado sus compras.");
                }
                case 2 -> System.out.println("Programa finalizado");
                default -> System.out.println("Opción inválida");
            }
        }
    }

}
