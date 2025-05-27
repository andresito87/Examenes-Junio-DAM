package tarea3pspsolucion;

/**
 *
 * @author LuisRosillo <>
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servidor HTTP que maneja juegos interactivos.
 *
 * Rutas disponibles:
 * - /adivina: Juega a "Adivina el Número".
 * - /dados: Juega a "Lanza Dados".
 * - /ppt: Juega a "Piedra, Papel o Tijera".
 */
public class ServidorHTTP {
    
    private static final ConcurrentHashMap<String, SesionJuego> sesiones = new ConcurrentHashMap<>();


    public static void main(String[] args) throws IOException {
        // Crea un servidor que escucha en el puerto 8066.
        ServerSocket serverSocket = new ServerSocket(8066);
        System.out.println("Servidor HTTP iniciado en el puerto 8066");
        System.out.println("Visita http://localhost:8066");

        // Bucle infinito para aceptar conexiones de clientes.
        while (true) {
            Socket cliente = serverSocket.accept(); // Espera y acepta una conexión entrante.
            Thread hiloCliente = new HiloCliente(cliente); // Crea un nuevo hilo para manejar al cliente.
            hiloCliente.start(); // Inicia el hilo.
        }
    }

    /**
     * Clase interna que implementa la lógica de manejar un cliente.
     * Extiende la clase Thread y sobrescribe el método run.
     */
    private static class HiloCliente extends Thread {
        private final Socket cliente;

        public HiloCliente(Socket cliente) {
            this.cliente = cliente; // Asocia el socket del cliente al hilo.
        }

        @Override
        public void run() {
            try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true, StandardCharsets.UTF_8)
            ) {
                // Lee la primera línea de la petición HTTP.
                String peticion = entrada.readLine();
                if (peticion == null || (!peticion.startsWith("GET") && !peticion.startsWith("POST"))) {
                    return; // Ignora la petición si no es GET o POST.
                }
                System.out.println("peticion: " + peticion);
                String ruta = peticion.split(" ")[1]; // Extrae la ruta solicitada.
                
                
                // Leer encabezados HTTP. Determina la sesionID y el tamaño del cuerpo.
                String[] metadatos = new String[2];
                metadatos = obtenerMetadatos(entrada);
                String sessionId = metadatos[0];
                SesionJuego sesion = sesiones.computeIfAbsent(sessionId, k -> new SesionJuego());
                
                int contentLength = Integer.parseInt(metadatos[1]);
                
                System.out.println("linea: vacía");    
                
                // Leer el cuerpo si es un POST.
                StringBuilder cuerpo = new StringBuilder(); // Para almacenar el cuerpo de la solicitud.
                if (peticion.startsWith("POST") && contentLength > 0) {
                    char[] buffer = new char[contentLength];
                    entrada.read(buffer, 0, contentLength);
                    cuerpo.append(buffer);
                }

                String respuesta; // Contendrá la respuesta generada por el servidor.

                if (ruta.equals("/")) {
                    respuesta = construirRespuesta(200, Paginas.html_index, sessionId);
                } else if (ruta.startsWith("/adivina")) {
                    respuesta = manejarAdivina(cuerpo.toString(), sesion, sessionId);
                } else if (ruta.startsWith("/dados")) {
                    respuesta = manejarDados(cuerpo.toString(), sesion, sessionId);
                } else if (ruta.startsWith("/ppt")) {
                    respuesta = manejarPPT(cuerpo.toString(), sesion, sessionId);
                } else {
                    respuesta = construirRespuesta(404, Paginas.html_noEncontrado, sessionId);
                }

                salida.println(respuesta); // Envía la respuesta al cliente.
            } catch (IOException e) {
                e.printStackTrace(); // Muestra errores en la consola.
            }
        }
        
        private String[] obtenerMetadatos(BufferedReader entrada) throws IOException {
            String linea;
            String[] metadatos = new String[2];
            String sessionId = null;
            String contentLength = "0";

            while (!(linea = entrada.readLine()).isBlank()) {
                System.out.println("Metadato: "+ linea);
                if (linea.startsWith("Cookie: ")) {
                    String[] cookies = linea.substring(8).split("; ");
                    for (String cookie : cookies) {
                        if (cookie.startsWith("sessionId=")) {
                            sessionId = cookie.substring(10);
                        }
                    }
                } else if (linea.startsWith("Content-Length: ")) {
                        contentLength = linea.substring(16);
                }
            }

            if (sessionId == null) {
                sessionId = UUID.randomUUID().toString();
            }
            //System.out.println("COOKIE: "+ sessionId);

            metadatos[0]=sessionId;
            metadatos[1]=contentLength;
            return metadatos;
        }

        private String manejarAdivina(String cuerpo, SesionJuego sesion, String sessionId) {
            if (sesion.numeroSecreto == 0) {
                sesion.numeroSecreto = new Random().nextInt(100) + 1; // Genera un número aleatorio al iniciar el juego.
                sesion.intentosAdivina = 0; // Resetea los intentos.
            }
            
            int codigo = 200;
            String respuestaHTML;
            try {
                if (!cuerpo.isEmpty()) {
                    System.out.println("Cuerpo Adivina: " + cuerpo);
                    int numeroUsuario = Integer.parseInt(cuerpo.split("=")[1]);
                    sesion.intentosAdivina++;

                    if (numeroUsuario == sesion.numeroSecreto) {
                        respuestaHTML = "<p>¡Felicidades! Has acertado el número " + sesion.numeroSecreto + " en " + sesion.intentosAdivina + " intentos.</p>";
                        sesion.numeroSecreto = 0; // Reinicia el juego.
                    } else if (sesion.intentosAdivina >= 10) {
                        respuestaHTML = "<p>No has acertado en 10 intentos. El número era " + sesion.numeroSecreto + ". Pulsa <a href='/adivina'>aquí</a> para reiniciar el juego.</p>";
                        sesion.numeroSecreto = 0; // Reinicia el juego.
                    } else {
                        respuestaHTML = "<p>Intento " + sesion.intentosAdivina + ": El número es " + (numeroUsuario < sesion.numeroSecreto ? "mayor" : "menor") + ".</p>";
                    }
                } else {
                    respuestaHTML = "<p>Introduce un número para empezar el juego.</p>";
                }
            } catch (Exception e) {
                respuestaHTML = "<p>Error procesando tu número. Intenta de nuevo.  </p>";
                codigo = 400;
                e.printStackTrace(); // Muestra errores en la consola.
            }

            return construirRespuesta(200, Paginas.generarHtmlAdivina(respuestaHTML), sessionId);
        }

        private String manejarDados(String cuerpo, SesionJuego sesion, String sessionId) {
            String resultado;
            int codigo = 200;
            System.out.println("cuerpoDados:" + cuerpo);

            try{
                if (!cuerpo.isEmpty()) {
                    Random random = new Random();
                    int dadoUsuario = Integer.parseInt(cuerpo.split("=")[1]);
                    int dadoServidor = random.nextInt(6) + 1;

                    if (dadoUsuario > dadoServidor) {
                        sesion.marcadorUsuarioDados++;
                        sesion.rondaDados++;
                        resultado = "<p>Ronda "+ sesion.rondaDados + " .Ganaste esta ronda. Tu dado: " + dadoUsuario + " - Dado del servidor: " + dadoServidor + "  </p>    ";
                    } else if (dadoUsuario < dadoServidor) {
                        sesion.marcadorServidorDados++;
                        sesion.rondaDados++;
                        resultado = "<p>Ronda "+ sesion.rondaDados + " .Perdiste esta ronda. Tu dado: " + dadoUsuario + " - Dado del servidor: " + dadoServidor + "  </p>    ";
                    } else {
                        resultado = "<p>Ronda "+ sesion.rondaDados + " .Empate en esta ronda. Ambos sacaron: " + dadoUsuario + "  </p>   ";
                    }

                    if (sesion.rondaDados == 5) {
                        if (sesion.marcadorUsuarioDados > sesion.marcadorServidorDados) {
                            resultado += "<p>¡Ganaste el juego! Marcador final: " + sesion.marcadorUsuarioDados + " - " + sesion.marcadorServidorDados + ". Vuelve a pulsar el botón para jugar de nuevo.</p>     ";
                        } else {
                            resultado += "<p>Perdiste el juego. Marcador final: " + sesion.marcadorUsuarioDados + " - " + sesion.marcadorServidorDados + ". Vuelve a pulsar el botón para jugar de nuevo.</p>     ";
                        }
                        sesion.marcadorUsuarioDados = 0;
                        sesion.marcadorServidorDados = 0;
                        sesion.rondaDados = 0;
                    }
                }else{
                    resultado = "<p>Pulsa el botón para lanzar los dados.  </p>    ";
                }
            
            } catch (Exception e) {
                resultado = "<p>Error procesando tu elección. Intenta de nuevo.</p>";
                codigo = 400;
                e.printStackTrace(); // Muestra errores en la consola.
            }

            return construirRespuesta(200, Paginas.generarHtmlDados(resultado), sessionId);
        }

        private String manejarPPT(String cuerpo, SesionJuego sesion, String sessionId) {

            String[] opciones = {"Piedra", "Papel", "Tijeras"};
            String resultado = "<p>Pulsa un botón para jugar.</p>  ";
            int codigo = 200;
            
            System.out.println("cuerpoPPT:" + cuerpo);
            try {
                if (!cuerpo.isEmpty()) {
                    String eleccionUsuario = cuerpo.split("=")[1];
                    String eleccionServidor = opciones[new Random().nextInt(3)];

                    if (eleccionUsuario.equals(eleccionServidor)) {
                        resultado = "<p>Ronda "+ sesion.rondaPPT + " .Empate en esta ronda. Ambos eligieron: " + eleccionServidor + "</p>  ";
                    } else if (
                        (eleccionUsuario.equals("Piedra") && eleccionServidor.equals("Tijeras")) ||
                        (eleccionUsuario.equals("Papel") && eleccionServidor.equals("Piedra")) ||
                        (eleccionUsuario.equals("Tijeras") && eleccionServidor.equals("Papel"))
                    ) {
                        sesion.marcadorUsuarioPPT++;
                        sesion.rondaPPT++;
                        resultado = "<p>Ronda "+ sesion.rondaPPT + " .Ganaste esta ronda. Elegiste: " + eleccionUsuario + " - El servidor eligió: " + eleccionServidor + "  </p>  ";
                    } else {
                        sesion.marcadorServidorPPT++;
                        sesion.rondaPPT++;
                        resultado = "<p>Ronda "+ sesion.rondaPPT + " .Perdiste esta ronda. Elegiste: " + eleccionUsuario + " - El servidor eligió: " + eleccionServidor + "  </p>  ";
                    }

                    if (sesion.rondaPPT == 5) {
                        if (sesion.marcadorUsuarioPPT > sesion.marcadorServidorPPT) {
                            resultado += "<p>¡Ganaste el juego! Marcador final: " + sesion.marcadorUsuarioPPT + " - " + sesion.marcadorServidorPPT + ". Vuelve a pulsar un botón para jugar de nuevo.</p>    ";
                        } else {
                            resultado += "<p>Perdiste el juego. Marcador final: " + sesion.marcadorUsuarioPPT + " - " + sesion.marcadorServidorPPT + ". Vuelve a pulsar un botón para jugar de nuevo.</p>    ";
                        }
                        sesion.marcadorUsuarioPPT = 0;
                        sesion.marcadorServidorPPT = 0;
                        sesion.rondaPPT=0;
                    }
                }
            } catch (Exception e) {
                resultado = "<p>Error procesando tu elección. Intenta de nuevo.</p>  ";
                codigo = 400;
                e.printStackTrace(); // Muestra errores en la consola.
            }

            return construirRespuesta(codigo, Paginas.generarHtmlPpt(resultado), sessionId);
        }

        private String construirRespuesta(int codigo, String contenido, String sessionId) {
            return (codigo == 200 ? "HTTP/1.1 200 OK" : "HTTP/1.1 400 Bad Request") + "\n"   //Linea inicial
                    + "Content-Type: text/html; charset=UTF-8"+ "\n"                         //Metadatos
                    + "Content-Length: " + contenido.length() + "\n"
                    + "Set-Cookie: sessionId=" + sessionId + "; Path=/;\n" 
                    + "Content-Type: text/html; charset=UTF-8\n" 
                    + "\n"                                                                   //Línea vacía
                    + contenido;                                                             //Cuerpo
        }
        
    }
}
