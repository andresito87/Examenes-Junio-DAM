
package tarea4pspsolucion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 *
 * @author LuisRosillo <>
 */
public class GestionLog {

    public static void configurarLog(FileHandler fh, Logger logger) throws SecurityException {

        // Sobreescribimos este método para definir el formato de salida
        fh.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss"));
                return String.format("%s %s%n", fechaHora, record.getMessage());
            }
        });

        // Asignamos el manejador del fichero
        logger.addHandler(fh);
        // No mostramos los mensaje por consola
        logger.setUseParentHandlers(true);
        // Establecemos el nivel de registro
        logger.setLevel(Level.ALL);

        // Hook para cerrar el FileHandles al finalizar la aplicación
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (fh != null) {
                fh.close();
            }
        }));
    }

}
