package cliente_servidor_farmacia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author andres
 */
public class Servidor {

    private static final int PUERTO = 60230;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // Abrir conexión
            ServerSocket ss = new ServerSocket(PUERTO);
            System.out.println("Servidor esperando clientes en el puerto " + PUERTO + " ...");

            while (true) { // Permitir múltiples clientes
                Socket socket = ss.accept();

                // Manejo de cada cliente que llega al servidor
                new Thread(new HiloServidor(socket)).start();

            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
}

class HiloServidor implements Runnable {

    private static int cantidadParacetamol = 10;
    private static int cantidadIbuprofeno = 10;
    private static int cantidadVitaminaC = 10;
    private final Socket socket;
    private static String nombreCliente;

    public HiloServidor(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Lectura de datos
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

            //Se recibe el producto que va a comprar el cliente
            nombreCliente = br.readLine();
            String producto = br.readLine();
            int cantidadProducto = Integer.parseInt(br.readLine());

            switch (producto) {
                case "Paracetamol" -> {
                    venderParacetamol(pw, cantidadProducto);
                }
                case "Ibuprofeno" -> {
                    venderIbuprofeno(pw, cantidadProducto);
                }
                case "Vitamina C" -> {
                    venderVitaminaC(pw, cantidadProducto);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al manejar el cliente: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket: " + e.getMessage());
            }
        }
    }

    private static synchronized void venderParacetamol(PrintWriter pw, int cantidad) {
        if (cantidadParacetamol >= cantidad) {
            cantidadParacetamol -= cantidad;
            System.out.println("Se venden " + cantidad + " Paracetamol, quedan " + cantidadParacetamol);
            String mensaje = "Cliente " + nombreCliente + " compra de " + cantidad + " paracetamol realizada correctamente.";
            pw.println(mensaje);
        } else {
            System.out.println("No se pudo vender " + cantidad + " Paracetamol a " + nombreCliente + ". Stock disponible: " + cantidadParacetamol);
            String mensaje = "Cliente " + nombreCliente + " lo sentimos, no tenemos " + cantidad + " de paracetamol.";
            pw.println(mensaje);
        }
        mostrarStock();
    }

    private static synchronized void venderIbuprofeno(PrintWriter pw, int cantidad) {
        if (cantidadIbuprofeno >= cantidad) {
            cantidadIbuprofeno -= cantidad;
            System.out.println("Se venden " + cantidad + " Ibuprofeno, quedan " + cantidadIbuprofeno);
            String mensaje = "Cliente " + nombreCliente + " compra de " + cantidad + " ibuprofeno realizada correctamente.";
            pw.println(mensaje);
        } else {
            System.out.println("No se pudo vender " + cantidad + " Ibuprofeno a " + nombreCliente + ". Stock disponible: " + cantidadIbuprofeno);
            String mensaje = "Cliente " + nombreCliente + " lo sentimos, no tenemos " + cantidad + " de ibuprofeno.";
            pw.println(mensaje);
        }
        mostrarStock();
    }

    private static synchronized void venderVitaminaC(PrintWriter pw, int cantidad) {
        if (cantidadVitaminaC >= cantidad) {
            cantidadVitaminaC -= cantidad;
            System.out.println("Se venden " + cantidad + " Vitamina C, quedan " + cantidadVitaminaC);
            String mensaje = "Cliente " + nombreCliente + " compra de " + cantidad + " vitamina C realizada correctamente.";
            pw.println(mensaje);
        } else {
            System.out.println("No se pudo vender " + cantidad + " Vitamina C a " + nombreCliente + ". Stock disponible: " + cantidadVitaminaC);
            String mensaje = "Cliente " + nombreCliente + " lo sentimos, no tenemos " + cantidad + " de vitamina C.";
            pw.println(mensaje);
        }
        mostrarStock();
    }

    private static void mostrarStock() {
        System.out.println("Stock disponible:\n -Paracetamol: " + cantidadParacetamol
                + "\n-Ibuprofeno: " + cantidadIbuprofeno
                + "\n-Vitamina C: " + cantidadVitaminaC);
    }
}
