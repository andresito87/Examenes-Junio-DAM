package proyectobasehotel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import static proyectobasehotel.Utilidades.registrarReserva;

public class ServidorHTTP {

    public static void main(String[] args) throws IOException {

        // Crear el servidor en el puerto 8066
        ServerSocket serverSocket = new ServerSocket(8066);
        System.out.println("Servidor HTTP iniciado en el puerto 8066...");
        System.out.println("Visita http://localhost:8066");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Cliente conectado...");
            Thread hiloServidor = new HiloCliente(socket);
            hiloServidor.start();
        }
    }
}

class HiloCliente extends Thread {

    private final Socket socket;

    public HiloCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream())); PrintWriter salida = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)) {
            String peticion = entrada.readLine();
            System.out.println("Petición: " + peticion);

            if (peticion == null || (!peticion.startsWith("GET") && !peticion.startsWith("POST"))) {
                enviarRespuesta(salida, 404, Paginas.HTML_NO_ENCONTRADO);
                return;
            }

            String ruta = obtenerRuta(peticion);
            System.out.println("Ruta: " + ruta);

            int contentLength = obtenerContent(entrada);

            String respuesta;

            if (peticion.startsWith("GET")
                    && (ruta.equals("/") || ruta.equals("/favicon.ico"))) {
                enviarRespuesta(salida, 200, Paginas.HTML_RESERVAS);
            } else if (peticion.startsWith("POST") && contentLength > 0) {
                respuesta = procesarPostRequest(entrada, contentLength);

                if (respuesta.equals("Cantidad invalida.")) {
                    enviarRespuesta(salida, 400, Paginas.HTML_ERROR);
                } else {
                    enviarRespuesta(salida, 200, Paginas.HTML_RESERVAS);
                }
            } else {
                enviarRespuesta(salida, 404, Paginas.HTML_NO_ENCONTRADO);
            }

        } catch (Exception e) {
            System.err.println("ERROR Hilo: " + e.getMessage());
        }
    }

    private void enviarRespuesta(PrintWriter writer, int codigo, String contenido) {
        String statusLine;

        switch (codigo) {
            case 200 ->
                statusLine = "HTTP/1.1 200 OK";
            case 400 ->
                statusLine = "HTTP/1.1 400 Bad Request";
            case 404 ->
                statusLine = "HTTP/1.1 404 Not Found";
            default -> {
                statusLine = "HTTP/1.1 500 Internal Server Error";
                contenido = "<h1>Error desconocido</h1>";
            }
        }

        writer.println(statusLine + "\n"
                + "Content-Type: text/html; charset=UTF-8\n"
                + "Content-Length: " + contenido.length() + "\n"
                + "\n"
                + contenido);
    }

    private int obtenerContent(BufferedReader entrada) throws IOException {
        int contentLength = 0;
        String linea;

        // Leer las líneas de los encabezados HTTP hasta encontrar "Content-Length"
        while (!(linea = entrada.readLine()).isEmpty()) {
            if (linea.startsWith("Content-Length:")) {
                String[] partes = linea.split(":");
                if (partes.length > 1) {
                    contentLength = Integer.parseInt(partes[1].trim());
                }
            }
        }
        return contentLength;
    }

    private String obtenerRuta(String peticion) {
        if (peticion != null && !peticion.isEmpty()) {
            String[] partes = peticion.split(" ");
            if (partes.length >= 2) {
                return partes[1];
            }
        }
        return "/"; // ruta por defecto
    }

    private String procesarPostRequest(BufferedReader entrada, int contentLength) throws IOException, Exception {
        char[] buffer = new char[contentLength];
        entrada.read(buffer, 0, contentLength);
        String cuerpo = new String(buffer);
        Map<String, String> datos = parsearFormulario(cuerpo);
        System.out.println(" Datos recibidos en POST: " + datos);
        return procesarReserva(datos);
    }

    public synchronized String procesarReserva(Map<String, String> datos) throws Exception {

        // Extraer el dia y cantidad del cuerpo de la petición
        String dia = datos.get("dia");
        int cantidad;
        try {
            cantidad = Integer.parseInt(datos.get("cantidad")); // Convertir la cantidad a número
        } catch (NumberFormatException e) {
            return "Cantidad invalida."; // Manejar error si la conversión falla
        }

        return registrarReserva(dia, cantidad)
                ? Paginas.HTML_RESERVAS
                : Paginas.HTML_NO_ENCONTRADO;
    }

    private Map<String, String> parsearFormulario(String cuerpo) throws UnsupportedEncodingException {
        Map<String, String> resultado = new HashMap<>();
        for (String pair : cuerpo.split("&")) { // Dividir los datos por `&`
            String[] kv = pair.split("=");
            String clave = URLDecoder.decode(kv[0], "UTF-8"); // Decodificar la clave
            String valor = kv.length > 1 ? URLDecoder.decode(kv[1], "UTF-8") : ""; // Decodificar el valor si existe
            resultado.put(clave, valor);
        }

        return resultado;
    }
}
