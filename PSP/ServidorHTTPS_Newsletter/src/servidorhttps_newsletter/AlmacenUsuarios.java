package chikitowiki_servidorhttps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 *
 * @author andres
 */
public class AlmacenFrases {

    private static final String ARCHIVO = "frases.txt";

    public String obtenerFrase() {
        Path path = Paths.get(ARCHIVO);

        if (!Files.exists(path)) {
            return "";
        }

        try {
            List<String> frases = Files.readAllLines(path);

            if (frases.isEmpty()) {
                return "";
            }

            int indice = (int)(Math.random() * frases.size());
            return frases.get(indice);

        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            return "";
        }
    }
}
