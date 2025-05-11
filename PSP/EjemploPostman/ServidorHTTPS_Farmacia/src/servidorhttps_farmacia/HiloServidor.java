package servidorhttps_farmacia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author andres
 */
public class HiloServidor implements Runnable {

    //Creando Almacen de usuarios e hilos escritores y lectores
    private Almacen almacen = null;

    private final Socket socketCliente;

    public HiloServidor(Socket sCliente, Almacen almacen) {
        this.socketCliente = sCliente;
        this.almacen = almacen;
    }

    @Override
    public void run() {
        try {
            String peticion;
            String html;

            //Obtenemos el flujo de entrada, lo usamos para leer mensajes del cliente
            InputStreamReader inSR = new InputStreamReader(
                    socketCliente.getInputStream());
            BufferedReader br = new BufferedReader(inSR);

            //Obtenemos el flujo se salida, lo usamos para escribir mensajes al cliente
            PrintWriter pw = new PrintWriter(
                    socketCliente.getOutputStream(), true);

            //Leemos la petición cliente
            peticion = br.readLine();

            if (peticion != null) {
                //Limpieza de la petición
                peticion = peticion.replaceAll(" ", "");

                //Peticiones GET
                if (peticion.startsWith("GET")) {
                    //extrae la subcadena entre 'GET' y 'HTTP/1.1'
                    peticion = peticion.substring(3, peticion.lastIndexOf("HTTP")).trim(); //OJO: 3 porque GET tiene tres letras
                    switch (peticion) {
                        case "/" -> {
                            String htmlFinal = Paginas.HTML_INDEX;
                            // Calculamos la longitud de bytes en UTF-8
                            byte[] htmlBytes = htmlFinal.getBytes(StandardCharsets.UTF_8);
                            // Enviamos la respuesta HTTP
                            pw.println(Mensajes.LINEA_INICIAL_OK);
                            pw.println(Paginas.PRIMERA_CABECERA);
                            pw.println();
                            pw.println(new String(htmlBytes, StandardCharsets.UTF_8));
                        }
                        case "/rellena-stock" -> {
                            almacen.guardar(10);
                            String htmlFinal = Paginas.HTML_STOCK_REPUESTO;
                            // Calculamos la longitud de bytes en UTF-8
                            byte[] htmlBytes = htmlFinal.getBytes(StandardCharsets.UTF_8);
                            // Enviamos la respuesta HTTP
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

                } else if (peticion.startsWith("POST")) {
                    // extrae la subcadena entre 'POST' y 'HTTP/1.1'
                    peticion = peticion.substring(4, peticion.lastIndexOf("HTTP")).trim(); //OJO: 4 porque POST tiene cuatro letras

                    //Leer las cabeceras hasta encontrar una línea vacía
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
                    String body;
                    if (contentLength > 0) {
                        char[] buffer = new char[contentLength];
                        int bytesRead = br.read(buffer, 0, contentLength);
                        if (bytesRead > 0) {
                            body = new String(buffer);
                            System.out.println("Cuerpo recibido: " + body);
                        } else {
                            System.out.println("Error: no se pudo leer el cuerpo de la petición.");
                            return;
                        }

                        // Extraer clasificar a que juego dirigir la petición
                        String producto = null;
                        int cantidad = 0;

                        String[] parametros = body.split("&");

                        for (String parametro : parametros) {
                            String[] claveValor = parametro.split("=");
                            // Viene un parámetro, el valor introducido para jugar a un juego
                            if (parametros.length == 2) {
                                if (claveValor[0].equals("producto") && claveValor.length > 1) {
                                    producto = claveValor[1];
                                } else if (claveValor[0].equals("cantidad") && claveValor.length > 1) {
                                    cantidad = Integer.parseInt(claveValor[1]);
                                }
                            }
                        }

                        // no vienen los parámetros necesarios, error
                        if (producto == null || cantidad == 0) {
                            html = Paginas.HTML_ERROR_INFO_ENVIADA;
                            pw.println(Mensajes.LINEA_INICIAL_BAD_REQUEST);
                            pw.println(Paginas.PRIMERA_CABECERA);
                            pw.println();
                            pw.println(html);
                        }// Si el usuario esta comprando
                        else if (peticion.contains("/")) {
                            boolean resultado = false;

                            producto = producto.equals("Vitamina_C") ? "Vitamina C" : producto;

                            if (cantidad <= almacen.leerCantidad(producto)) {
                                resultado = almacen.accesoEscribir(producto, cantidad);
                                almacen.escrituraFinalizada();
                            }

                            String htmlFinal = Paginas.generarMensaje(resultado, producto, cantidad);
                            // Calculamos la longitud de bytes en UTF-8
                            byte[] htmlBytes = htmlFinal.getBytes(StandardCharsets.UTF_8);
                            // Enviamos la respuesta HTTP
                            pw.println(Mensajes.LINEA_INICIAL_OK);
                            pw.println(Paginas.PRIMERA_CABECERA);
                            pw.println();
                            pw.println(new String(htmlBytes, StandardCharsets.UTF_8));
                        }
                    }
                } else {
                    html = Paginas.HTML_NO_ENCONTRADO;
                    pw.println(Mensajes.LINEA_INICIAL_NOT_FOUND);
                    pw.println(Paginas.PRIMERA_CABECERA);
                    pw.println();
                    pw.println(html);
                }
            }
        } catch (IOException ex) {
            // System.out.println(ex.getMessage());
        } finally {
            try {
                //Cierra la conexión entrante
                if (socketCliente != null) {
                    socketCliente.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
