package servidorhttps_newsletter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author ANDRÉS SAMUEL PODADERA GONZÁLEZ
 */
public class HiloServidor implements Runnable {

    //Creando Almacen de usuarios e hilos escritores y lectores
    private AlmacenUsuarios almacen = null;

    private final Socket socketCliente;

    public HiloServidor(Socket sCliente, AlmacenUsuarios almacen) {
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
                        case "/listado" -> {
                            String listado = almacen.obtenerDatosUsuarios();
                            almacen.lecturaFinalizada();
                            String htmlFinal = Paginas.generarHtmlConMensaje(listado);
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
                    if (peticion.equals("/") && body != null && body.contains("&")) {
                        String[] parametros = body.split("&");
                        String[] claveValorUsuario = parametros[0].split("=");
                        String[] claveValorEmail = parametros[1].split("=");
                        if (claveValorUsuario.length == 2 && "usuario".equals(claveValorUsuario[0])
                                && claveValorEmail.length == 2 && "email".equals(claveValorEmail[0])) {
                            String[] datosUsuario = {claveValorUsuario[1], decodificarUTF8(claveValorEmail[1])};
                            String resultado = almacen.accesoEscribir(datosUsuario);
                            almacen.escrituraFinalizada();
                            String htmlFinal = Paginas.generarHtmlConMensaje(resultado);
                            byte[] htmlBytes = htmlFinal.getBytes(StandardCharsets.UTF_8);
                            pw.println(Mensajes.LINEA_INICIAL_OK);
                            pw.println(Paginas.PRIMERA_CABECERA);
                            pw.println();
                            pw.println(new String(htmlBytes, StandardCharsets.UTF_8));
                        } else {
                            html = Paginas.HTML_INFORMACION_INVALIDA;
                            pw.println(Mensajes.LINEA_INICIAL_BAD_REQUEST);
                            pw.println(Paginas.PRIMERA_CABECERA);
                            pw.println();
                            pw.println(html);
                        }

                    } else {
                        html = Paginas.HTML_INFORMACION_INVALIDA;
                        pw.println(Mensajes.LINEA_INICIAL_BAD_REQUEST);
                        pw.println(Paginas.PRIMERA_CABECERA);
                        pw.println();
                        pw.println(html);
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

    public static String decodificarUTF8(String parametro) {
        try {
            return URLDecoder.decode(parametro, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error al intentar decodificar el párametro recibido " + parametro);
            return parametro; // devuelve el valor original en caso de error
        }
    }
}
