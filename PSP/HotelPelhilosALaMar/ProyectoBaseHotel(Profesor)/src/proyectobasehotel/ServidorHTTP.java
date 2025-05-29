package proyectobasehotel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorHTTP {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(12349);
        System.out.println("Servidor iniciado en el puerto 12349. Esperando reservas...");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(new HiloServidor(clientSocket)).start();
        }
    }
}

class HiloServidor extends Thread {
    private final Socket cliente;

    public HiloServidor(Socket cliente) {
        this.cliente = cliente;
    }

    @Override
    public void run() {

        try (BufferedReader entrada = 
                new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                PrintWriter salida = 
                        new PrintWriter(cliente.getOutputStream(), true)) {
            
            String peticion = entrada.readLine(); // Lee la primera lí­nea de la petición HTTP.
            if (peticion != null 
                    && (!peticion.startsWith("GET") 
                    || !peticion.startsWith("POST"))) {
                String ruta = peticion.split(" ")[1]; // Extrae la ruta solicitada.
                StringBuilder cuerpo = new StringBuilder(); // Para almacenar el cuerpo de la solicitud.
                String linea;

                // Leer encabezados HTTP y determinar el tamaño del cuerpo.
                int contentLength = 0;
                while (!(linea = entrada.readLine()).isBlank()) {
                    if (linea.startsWith("Content-Length: ")) {
                        contentLength = Integer.parseInt(linea.substring(16));
                    }
                }

                // Leer el cuerpo si es un POST.
                if (peticion.startsWith("POST") && contentLength > 0) {
                    char[] buffer = new char[contentLength];
                    entrada.read(buffer, 0, contentLength);
                    cuerpo.append(buffer);

                    String dato1 = cuerpo.toString().split("&")[0];
                    String dato2 = cuerpo.toString().split("&")[1];

                    String dia = dato1.split("=")[1];
                    int numero = Integer.parseInt(dato2.split("=")[1]);
                    System.out.println("La nueva reserva ha sido para el " + dia + " con " + numero + " habitaciones.");
                    GestorFichero.actualizarFichero(dia, numero);
                }

                String respuesta;
                if (ruta.equals("/")) {
                    respuesta = construirRespuesta(200, Paginas.html_reservas);
                } else {
                    respuesta = construirRespuesta(404, Paginas.html_noEncontrado);
                }
                salida.println(respuesta);
                cliente.close(); // cerrar el socket
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private String construirRespuesta(int codigo, String html) {
        String lineaInicial = codigo == 200 ? "HTTP/1.1 200 OK" : "HTTP/1.1 404 Not Found";
        return lineaInicial + "\n"
                + "Content-Type: text/html; charset=UTF-8"
                + "\nContent-Length: " + html.length()
                + "\n\n"
                + html;
    }
}
