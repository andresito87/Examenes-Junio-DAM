package chikitowiki_servidorhttps;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author andres
 */
public class LoggerHelper {
    private static final Logger LOGGER = Logger.getLogger("Logs");
    private static boolean estaConfigurado = false;
    private static FileHandler fileHandler;

    static {
        configurarLogger();
        estaConfigurado = true;
    }

    /**
     * Configura el logger con los parámetros deseados
     */
    private static void configurarLogger() {
        try {
            // Configuramos el logger una sóla vez
            if (!estaConfigurado) {

                // Configurar manejadores
                fileHandler = new FileHandler("logErrores.txt", true);
                ConsoleHandler consoleHandler = new ConsoleHandler();

                // Creamos el formateador de los mensajes
                Formatter logFormatter = new Formatter() {
                    @Override
                    public synchronized String format(LogRecord record) {
                        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                        return String.format("%s - %s%n", fechaHora, record.getMessage());
                    }
                };

                // Asignamos el formateador
                fileHandler.setFormatter(logFormatter);
                consoleHandler.setFormatter(logFormatter);

                // Configurar niveles de log
                fileHandler.setLevel(Level.ALL);
                consoleHandler.setLevel(Level.ALL);

                // Agregamos manejadores al logger
                LOGGER.addHandler(fileHandler);
                LOGGER.addHandler(consoleHandler);

                // Desactivamos los loggers del sistema, usaremos los nuestros
                LOGGER.setUseParentHandlers(false);

                // Indicamos el nivel mínimo de los logs que vamos a registrar
                LOGGER.setLevel(Level.ALL);

                // Agregar Shutdown Hook para cerrar correctamente el FileHandler
                Runtime.getRuntime().addShutdownHook(new Thread(LoggerHelper::cerrarLogger));
            }

        } catch (IOException e) {
            System.err.println("Error al configurar el logger: " + e.getMessage());
        }
    }

    /**
     * Realiza la escritura del log con nivel y mensaje de forma concurrente en
     * el archivo
     *
     * @param level
     * @param mensaje
     */
    public static synchronized void log(Level level, String mensaje) {
        LOGGER.log(level, mensaje);
    }

    /**
     * Cierra el FileHandler correctamente al finalizar el programa.
     */
    public static void cerrarLogger() {
        if (fileHandler != null) {
            System.out.println("Cerrando FileHandler...");
            LOGGER.removeHandler(fileHandler);
            fileHandler.close();
        }
    }
}
