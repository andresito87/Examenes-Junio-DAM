package chikitowiki_servidorhttps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

/**
 *
 * @author andres
 */
public class HiloServidor implements Runnable {

    //Creando Almacen de usuarios e hilos escritores y lectores
    private AlmacenFrases almacen = null;

    private final Socket socketCliente;

    public HiloServidor(Socket sCliente, AlmacenFrases almacen) {
        this.socketCliente = sCliente;
        this.almacen = almacen;
    }

    @Override
    public void run() {
        try {
            String peticion;
            String html;

            //Obtenemos el flujo de entrada, lo usamos para leer mensajes del cliente
            InputStreamReader inSR = new InputStreamReader(socketCliente.getInputStream());
            BufferedReader br = new BufferedReader(inSR);

            //Obtenemos el flujo de salida, lo usamos para escribir mensajes al cliente
            PrintWriter pw = new PrintWriter(socketCliente.getOutputStream(), true);

            //Leemos la petición cliente
            peticion = br.readLine();

            if (peticion != null) {
                peticion = peticion.replaceAll(" ", "");

                // Peticiones GET
                if (peticion.startsWith("GET")) {
                    peticion = peticion.substring(3, peticion.lastIndexOf("HTTP")).trim(); // extraemos la URL
                    switch (peticion) {
                        case "/" -> {
                            String htmlFinal = Paginas.HTML_INDEX;
                            byte[] htmlBytes = htmlFinal.getBytes(StandardCharsets.UTF_8);
                            pw.println(Mensajes.LINEA_INICIAL_OK);
                            pw.println(Paginas.PRIMERA_CABECERA);
                            pw.println();
                            pw.println(new String(htmlBytes, StandardCharsets.UTF_8));
                        }
                        default -> {
                            html = Paginas.HTML_NO_ENCONTRADO;
                            pw.println(Mensajes.LINEA_INICIAL_NOT_FOUND);
                            pw.println(Paginas.PRIMERA_CABECERA);
                            pw.println();
                            pw.println(html);
                        }
                    }
                } // Peticiones POST
                else if (peticion.startsWith("POST")) {
                    peticion = peticion.substring(4, peticion.lastIndexOf("HTTP")).trim(); // extraemos la URL

                    // Leer las cabeceras hasta encontrar una línea vacía
                    String linea;
                    int contentLength = 0;

                    // Leer las cabeceras para obtener Content-Length
                    while ((linea = br.readLine()) != null && !linea.isEmpty()) {
                        if (linea.toLowerCase().startsWith("content-length:")) {
                            try {
                                contentLength = Integer.parseInt(linea.split(":")[1].trim());
                            } catch (NumberFormatException e) {
                                System.out.println("Error al leer Content-Length: " + e.getMessage());
                            }
                        }
                    }

                    // Leer el cuerpo de la petición
                    String body = null;
                    if (contentLength > 0) {
                        char[] buffer = new char[contentLength];
                        int bytesRead = br.read(buffer, 0, contentLength);
                        if (bytesRead > 0) {
                            body = new String(buffer);
                            System.out.println("Cuerpo recibido: " + body);
                        }
                    }

                    // Procesar la clave-valor de la petición POST
                    if (peticion.equals("/") && body != null && body.contains("=")) {
                        String[] parametros = body.split("&");
                        for (String parametro : parametros) {
                            String[] claveValor = parametro.split("=");
                            if (claveValor.length == 2 && "frase".equals(claveValor[0]) && "true".equals(claveValor[1])) {
                                String frase = almacen.obtenerFrase();
                                String htmlFinal = Paginas.generarHtmlConFrase(frase);
                                byte[] htmlBytes = htmlFinal.getBytes(StandardCharsets.UTF_8);
                                pw.println(Mensajes.LINEA_INICIAL_OK);
                                pw.println(Paginas.PRIMERA_CABECERA);
                                pw.println();
                                pw.println(new String(htmlBytes, StandardCharsets.UTF_8));

                                // Guardamos el mensaje informativo en el registro
                                 LoggerHelper.log(Level.INFO,"Petición recibida y procesa por el servidor");
                            } else {
                                html = Paginas.HTML_INFORMACION_INVALIDA;
                                pw.println(Mensajes.LINEA_INICIAL_BAD_REQUEST);
                                pw.println(Paginas.PRIMERA_CABECERA);
                                pw.println();
                                pw.println(html);

                                // Guardamos el mensaje de error en el registro
                                 LoggerHelper.log(Level.SEVERE,"Error al procesar la petición recibida");
                            }
                        }
                    } else {
                        html = Paginas.HTML_INFORMACION_INVALIDA;
                        pw.println(Mensajes.LINEA_INICIAL_BAD_REQUEST);
                        pw.println(Paginas.PRIMERA_CABECERA);
                        pw.println();
                        pw.println(html);

                        // Guardamos el mensaje de error en el registro
                        LoggerHelper.log(Level.SEVERE,"Error al procesar la petición recibida");
                    }
                } // Cualquier otro tipo de peticion no se encuentra disponible en el servidor 
                else {
                    html = Paginas.HTML_NO_ENCONTRADO;
                    pw.println(Mensajes.LINEA_INICIAL_NOT_FOUND);
                    pw.println(Paginas.PRIMERA_CABECERA);
                    pw.println();
                    pw.println(html);
                }
            }
        } catch (IOException ex) {
            //System.out.println(ex.getMessage());
        } finally {
            try {
                if (socketCliente != null) {
                    socketCliente.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
