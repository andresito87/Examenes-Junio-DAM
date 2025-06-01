package ejercicio_lavanderia_hilos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimuladorClientes {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<Thread> hilos = new ArrayList<>();
        Lavanderia lavanderia = new Lavanderia();
        int opcion = 0;
        int contadorClientes = 0;
        boolean lavanderiaCerrada = false;

        // El menu solo se muestra una vez para no colapsar la terminal con demasiados mensajes
        System.out.println("\nBienvenid@ a la lavandería. Elige una de las opciones:");
        System.out.println("1. Simular peticiones clientes");
        System.out.println("2. Mostrar estado de lavandería");
        System.out.println("3. Cerrar lavandería");
        System.out.print("Seleccione una opción: ");

        do {

            opcion = scanner.nextInt();

            switch (opcion) {
                case 1 -> {
                    if (lavanderiaCerrada) {
                        // Con la lavanderia cerrada no vamos a permitir más clientes
                        System.out.println("La lavandería está cerrada. No se pueden simular más clientes.");
                    } else {
                        // Con la lavanderia abierta permitimos la entrada a los clientes
                        System.out.print("¿Cuántos clientes quieres simular?: ");
                        int cantidadClientes = scanner.nextInt();

                        // Creamos los hilos y los vamos lanzando
                        for (int i = 0; i < cantidadClientes; i++) {
                            contadorClientes++;
                            Thread hilo = new Thread(new Cliente("Cliente " + contadorClientes, lavanderia));
                            hilos.add(hilo);
                            hilo.start();
                        }
                    }
                }
                case 2 -> {
                    // Mostrar estado de la lavandría
                    lavanderia.mostrarEstado();
                }
                case 3 -> {
                    // Esperar a que todos los hilos terminen y cerrrar lavandería
                    System.out.println("Cerrando lavandería y ya no se permite mostrar su estado...");
                    lavanderiaCerrada = true;
                    System.out.println("Esperando a que los clientes terminen...");
                    // Esperamos a que todos los hilos acaben
                    for (Thread hilo : hilos) {
                        try {
                            hilo.join();
                        } catch (InterruptedException e) {
                            System.err.println("Error al esperar el hilo: " + e.getMessage());
                        }
                    }
                }

                default ->
                    System.out.println("Opción inválida");
            }
        } while (opcion != 3 || !lavanderiaCerrada);

        // Mensaje final
        System.out.println("Todos los clientes han terminado. Lavandería cerrada.");
    }
}
