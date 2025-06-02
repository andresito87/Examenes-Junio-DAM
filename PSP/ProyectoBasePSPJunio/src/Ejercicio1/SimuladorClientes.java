package Ejercicio1;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase que nos permite lanzar varios hilos cliente de forma concurrente
 *
 * @author Andrés Samuel Podadera González
 */
public class SimuladorClientes {

    public static void main(String[] args) {

        final String ROJO = "\033[1;31;47m";
        final String RESET = "\u001B[0m";

        Scanner scanner = new Scanner(System.in); // entrada de datos
        List<Thread> hilosClientes = new ArrayList<>(); // lista para almacenar todos los surfistas
        Tienda tienda = new Tienda("SurFun"); // creo el recurso compartido
        int opcion;
        int contadorSurfistas = 0;
        boolean tiendaCerrada = false;

        System.out.println(ROJO + "Bienvenid@ a " + tienda.getNombre() + ". Elige una de las opciones:" + RESET);
        System.out.println(ROJO + "1.- Llegada de clientes." + RESET);
        System.out.println(ROJO + "2.- Estado de tienda." + RESET);
        System.out.println(ROJO + "3.- Cerrar tienda." + RESET);

        do {
            opcion = scanner.nextInt();

            switch (opcion) {
                // lanzar hilos clientes
                case 1 -> {
                    if (tiendaCerrada) {
                        System.out.println("Lo sentimos la tienda esta cerrada.");
                    } else {
                        System.out.print("¿Cuántos clientes llegan a la vez? ");

                        int cantidadClientes = scanner.nextInt();

                        // creamos los hilos y los lanzamos
                        for (int i = 0; i < cantidadClientes; i++) {
                            contadorSurfistas++;
                            Thread hilo = new Thread(new Cliente("Surfista " + contadorSurfistas, tienda));
                            hilosClientes.add(hilo);
                            hilo.start();
                        }
                    }
                }
                // mostrar estado
                case 2 -> {
                    tienda.mostrarEstado();
                }
                // cerrar tienda
                case 3 -> {
                    System.out.println("Cerramos tienda. Esperamos a que nos devuelvan las tablas...");
                    tiendaCerrada = true; // con la tienda cerrada no permitimos que nos consulten el menú
                    // esperar a que todos los hilos terminen
                    for (Thread hilo : hilosClientes) {
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
        } while (opcion != 3 || !tiendaCerrada);

        // mensaje final
        System.out.println(ROJO + "Todos los surfistas entregaron sus tablas. Tienda cerrada." + RESET);
    }
}
