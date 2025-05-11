package servidorhttps_farmacia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author andres
 */
public class Almacen {

    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    private static final String ARCHIVO = "stock.txt";

    public final static int LIBRE = 0;
    public final static int CON_LECTORES = 1;
    public final static int CON_ESCRITOR = 2;

    private int estado = LIBRE;
    private int numLectores = 0;

    public synchronized boolean accesoEscribir(String producto, int cantidad) {
        if (estado == LIBRE) {
            estado = CON_ESCRITOR;
            this.guardar(producto, cantidad);
        } else {
            while (estado != LIBRE) {
                try {
                    // no está libre, el hilo permanece a la espera
                    wait();
                } catch (InterruptedException e) {
                    System.out.println(e);
                    return false;
                }
            }
            estado = CON_ESCRITOR;
            this.guardar(producto, cantidad);
        }
        System.out.println(ANSI_GREEN + "Hilo guarda la información en stock.txt" + ANSI_RESET);
        return true;
    }

    public synchronized boolean accesoEscribir(int cantidad) {
        if (estado == LIBRE) {
            estado = CON_ESCRITOR;
            this.guardar(cantidad);
        } else {
            while (estado != LIBRE) {
                try {
                    // no está libre, el hilo permanece a la espera
                    wait();
                } catch (InterruptedException e) {
                    System.out.println(e);
                    return false;
                }
            }
            estado = CON_ESCRITOR;
            this.guardar(cantidad);
        }
        System.out.println(ANSI_GREEN + "Hilo guarda la información en stock.txt" + ANSI_RESET);
        return true;
    }

    public int leerCantidad(String producto) {

        Path path = Paths.get(ARCHIVO);

        // Si el archivo no existe, crearlo y escribir los medicamentos con 10 unidades
        if (!Files.exists(path)) {
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write("Paracetamol:10\n");
                writer.write("Ibuprofeno:10\n");
                writer.write("Vitamina C:10\n");
            } catch (IOException e) {
                System.err.println("Error al crear el archivo: " + e.getMessage());
                return 0;
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(":");
                if (partes[0].trim().equalsIgnoreCase(producto)) {
                    return Integer.parseInt(partes[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return 0; // Si no se encuentra el producto
    }

    public boolean guardar(String producto, int cantidadARestar) {
        Map<String, Integer> stock = new HashMap<>();

        // Leer todo el archivo y cargar en el mapa
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(":");
                String nombre = partes[0].trim();
                int cantidad = Integer.parseInt(partes[1].trim());
                stock.put(nombre, cantidad);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return false;
        }

        // Actualizar el stock solo si no se vuelve negativo
        if (stock.containsKey(producto)) {
            int actual = stock.get(producto);
            if (actual >= cantidadARestar) {
                stock.put(producto, actual - cantidadARestar);
            } else {
                System.out.println("No hay suficiente stock de " + producto);
                return false;
            }
        }

        System.out.println(ANSI_GREEN + "Stock actual de medicamentos: ");
        for (Map.Entry<String, Integer> entry : stock.entrySet()) {
            System.out.println(ANSI_GREEN + "- " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println(ANSI_RESET);

        // Reescribir archivo con los valores nuevos
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (Map.Entry<String, Integer> entry : stock.entrySet()) {
                pw.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
            return false;
        }

        return true;
    }

    public boolean guardar(int cantidad) {
        Map<String, Integer> stock = new HashMap<>();

        // Asignar la misma cantidad a los tres productos
        stock.put("Paracetamol", cantidad);
        stock.put("Ibuprofeno", cantidad);
        stock.put("Vitamina C", cantidad);

        System.out.println(ANSI_GREEN + "Stock actualizado de todos los medicamentos:");
        for (Map.Entry<String, Integer> entry : stock.entrySet()) {
            System.out.println(ANSI_GREEN + "- " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println(ANSI_RESET);

        // Reescribir el archivo con los nuevos valores
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (Map.Entry<String, Integer> entry : stock.entrySet()) {
                pw.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo: " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Se utiliza para indicar que la escritura de un hilo ha terminado
     */
    public synchronized void escrituraFinalizada() {
        estado = LIBRE;
        notifyAll();
    }

    /**
     * Se utiliza para indicar que la lectura de un hilo ha terminado
     */
    public synchronized void lecturaFinalizada() {
        numLectores--;
        if (numLectores == 0) {
            estado = LIBRE;
            notifyAll();
        }
    }
}
