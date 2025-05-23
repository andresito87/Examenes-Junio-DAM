package hotelpelihilosalamar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author ANDRÉS SAMUEL PODADERA GONZÁLEZ
 */
public class ServidorHTTP {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        // Crea un servidor que escucha en el puerto 8066.
        ServerSocket serverSocket = new ServerSocket(8066);
        System.out.println("Servidor HTTP iniciado en el puerto 8066");
        System.out.println("Visita http://localhost:8066");

        // Bucle infinito para aceptar conexiones de clientes.
        while (true) {
            Socket cliente = serverSocket.accept(); // Espera y acepta una conexión entrante.
            Thread hiloServidor = new HiloServidor(cliente); // Crea un nuevo hilo para manejar al nuevo cliente.
            hiloServidor.start(); // Inicia el hilo.
        }
    }

    /**
     * Clase interna que implementa la lógica de manejar un cliente. Extiende la
     * clase Thread y sobrescribe el método run.
     */
    private static class HiloServidor extends Thread {

        private final Socket cliente;
        private static final AlmacenReservas almacen = new AlmacenReservas();

        public HiloServidor(Socket cliente) {
            this.cliente = cliente; // Asocia el socket del cliente al hilo.
        }

        @Override
        public void run() {
            try (BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream())); PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true, StandardCharsets.UTF_8)) {
                // Lee la primera línea de la petición HTTP.
                String peticion = entrada.readLine();
                if (peticion == null || (!peticion.startsWith("GET") && !peticion.startsWith("POST"))) {
                    return; // Ignora la petición si no es GET o POST.
                }
                System.out.println("peticion: " + peticion);
                String ruta = peticion.split(" ")[1]; // Extrae la ruta solicitada.
                String linea;

                // Leer encabezados HTTP y determinar el tamaño del cuerpo.
                int contentLength = 0;
                while (!(linea = entrada.readLine()).isBlank()) {
                    if (linea.startsWith("Content-Length: ")) {
                        contentLength = Integer.parseInt(linea.substring(16));
                    }
                }

                // Leer el cuerpo si es un POST.
                StringBuilder cuerpo = new StringBuilder(); // Para almacenar el cuerpo de la solicitud.
                if (peticion.startsWith("POST") && contentLength > 0) {
                    char[] buffer = new char[contentLength];
                    entrada.read(buffer, 0, contentLength);
                    cuerpo.append(buffer);
                }

                System.out.println(cuerpo);

                String respuesta; // Contendrá la respuesta generada por el servidor.

                if (ruta.equals("/") && peticion.startsWith("GET")) {
                    respuesta = construirRespuesta(200, Paginas.html_reservas);
                } else if (ruta.equals("/") && peticion.startsWith("POST")) {
                    String[] parametros = cuerpo.toString().split("&");
                    String dia = parametros[0].split("=")[1];
                    int cantidad;
                    try {
                        cantidad = Integer.parseInt(parametros[1].split("=")[1]);
                        almacen.accesoEscribir(dia, cantidad);
                        almacen.escrituraFinalizada();
                    } catch (NumberFormatException ex) {
                        System.out.println("Error en los datos enviados");
                    }
                    respuesta = construirRespuesta(200, Paginas.html_reservas);
                } else {
                    respuesta = construirRespuesta(404, Paginas.html_noEncontrado);
                }
                //System.out.println("Respuesta: " + respuesta);
                salida.println(respuesta); // Envía la respuesta al cliente.
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        private String construirRespuesta(int codigo, String contenido) {
            return (codigo == 200 ? "HTTP/1.1 200 OK" : "HTTP/1.1 400 Bad Request") + "\n" //Linea inicial
                    + "Content-Type: text/html; charset=UTF-8" + "\n" //Metadatos
                    + "Content-Length: " + contenido.length() + "\n"
                    + "\n" //Línea vacía
                    + contenido;             //Cuerpo
        }
    }

}
