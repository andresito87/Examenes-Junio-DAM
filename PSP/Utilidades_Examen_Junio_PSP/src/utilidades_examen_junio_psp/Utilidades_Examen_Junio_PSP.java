package utilidades_examen_junio_psp;

import java.io.IOException;
import java.nio.file.*;

public class Utilidades_Examen_Junio_PSP {

    public static void main(String[] args) throws IOException {

        ////********************************************************************
        Path path = Paths.get("archivo.txt");
        byte[] bytes = "Hola mundo".getBytes();

        if (Files.exists(path) && Files.size(path) > 0) {
            // Entra si el fichero existe y tiene contenido
            byte[] contenidoCifrado = Files.readAllBytes(path); // lee el archivo cifrado
            // el contenido del archivo en bytes se lo pasariamos al cifrar o descifrar
        }

        // Si el archivo no existe, lo crea
        Files.write(path, bytes, StandardOpenOption.CREATE);
        // Si el archivo ya existe, agrega el nuevo contenido al final sin borrar lo anterior
        Files.write(path, bytes, StandardOpenOption.APPEND);
        //**********************************************************************

    }

}
