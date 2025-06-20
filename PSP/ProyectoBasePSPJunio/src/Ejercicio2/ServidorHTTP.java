package Ejercicio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Clase que crea un servidor web HTTP
 *
 * @author Andres Samuel Podadera González
 */
public class ServidorHTTP {

    private static final int PUERTO = 8081;

    public static void main(String[] args) throws Exception {
        // creamos el servidor http en el puerto 8081
        ServerSocket serverSocket = new ServerSocket(PUERTO);
        System.out.println("Servidor iniciado en el puerto " + PUERTO + ". Esperando ventas...");
        while (true) {
            Socket clientSocket = serverSocket.accept(); // nos ponemos a la escucha de peticiones
            new Thread(new HiloCliente(clientSocket)).start();
        }
    }
}

/**
 * Clase que gestionará la lógica que realiza un cliente dentro de la tienda
 *
 * @author Andres Samuel Podadera González
 */
class HiloCliente extends Thread {

    private final Socket cliente;

    public HiloCliente(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {
        // creamos los buffer de entrada y salida de datos
        try (BufferedReader entrada
                = new BufferedReader(new InputStreamReader(cliente.getInputStream())); PrintWriter salida
                = new PrintWriter(cliente.getOutputStream(), true)) {

            // leemos la primera línea de las cabeceras
            String peticion = entrada.readLine();
            if (peticion != null && (!peticion.startsWith("GET") || !peticion.startsWith("POST"))) {
                String ruta = peticion.split(" ")[1];
                StringBuilder cuerpo = new StringBuilder();
                String linea;

                // Leer content-length
                // si la petición tiene content lenght mayor a 0 es que es una petición POST
                // las GET no tiene cuerpo
                int contentLength = 0;
                while (!(linea = entrada.readLine()).isBlank()) {
                    if (linea.startsWith("Content-Length: ")) {
                        // substring 16 para saltar directamente a la cantidad de content-lenght
                        contentLength = Integer.parseInt(linea.substring(16));
                    }
                }

                // Leer el cuerpo si es un POST
                if (peticion.startsWith("POST") && contentLength > 0) {
                    char[] buffer = new char[contentLength];
                    entrada.read(buffer, 0, contentLength);
                    cuerpo.append(buffer);

                    // obtenemos la cantidad de ganancia de la petición, convertida a punto flotante
                    float cantidad = Float.parseFloat(cuerpo.toString().split("=")[1]);
                    System.out.println("La nueva venta ha sido de " + cantidad);
                    // actualizamos el fichero con la nueva cantidad
                    GestorFichero.actualizarFichero(cantidad);
                }

                // contruir la respuesta
                String respuesta;
                if (ruta.equals("/")) {
                    respuesta = construirRespuesta(200, Paginas.html_tienda);
                } else {
                    respuesta = construirRespuesta(404, Paginas.html_noEncontrado);
                }
                salida.println(respuesta); // enviar respuesta
                cliente.close();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Método que permite construir una respuesta HTTP
     *
     * @param codigo
     * @param html
     * @return contenido de la respuesta http con sus cabeceras y su cuerpo en
     * HTML
     */
    private String construirRespuesta(int codigo, String html) {
        String lineaInicial = codigo == 200 ? "HTTP/1.1 200 OK" : "HTTP/1.1 404 Not Found";
        return lineaInicial + "\n"
                + "Content-Type: text/html; charset=UTF-8"
                + "\nContent-Length: " + html.length()
                + "\n\n"
                + html;
    }
}
