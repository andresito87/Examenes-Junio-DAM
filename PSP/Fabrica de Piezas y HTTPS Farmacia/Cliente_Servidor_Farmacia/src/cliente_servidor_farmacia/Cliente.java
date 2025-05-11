package cliente_servidor_farmacia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author andres
 */
public class Cliente extends Thread {

    private static final String HOST = "localhost";
    private static final int PUERTO = 60230;
    private final String nombre;
    private final String nombreProductoAComprar;
    private final int cantidadProducto;

    public Cliente(String nombre,
            String nombreProductoAComprar,
            int cantidadProducto
    ) {
        this.nombre = nombre;
        this.nombreProductoAComprar = nombreProductoAComprar;
        this.cantidadProducto = cantidadProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNombreProductoAComprar() {
        return nombreProductoAComprar;
    }

    public int getCantidadProducto() {
        return cantidadProducto;
    }

    @Override
    public void run() {
        try {
            //Abrir conexión
            Socket clienteSocket = new Socket(HOST, PUERTO);

            //Lectura
            BufferedReader br = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));

            //Escritura
            PrintWriter pw = new PrintWriter(clienteSocket.getOutputStream(), true);

            String linea = "";

            // cliente envia su nombre, el producto y la cantidad
            pw.println(this.nombre);
            pw.println(this.nombreProductoAComprar);
            pw.println(this.cantidadProducto);

            // cliente obtiene resultado de la petición y lo muestra
            String resultado = br.readLine();
            System.out.println(resultado);

        } catch (IOException e) {
            System.err.println("Error al conectar al servidor: " + e.getMessage());
        }

    }
}
