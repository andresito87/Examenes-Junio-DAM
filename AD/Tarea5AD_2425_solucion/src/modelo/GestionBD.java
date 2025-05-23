/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.basex.core.BaseXException;
import org.basex.core.Context;
import org.basex.core.cmd.CreateDB;
import org.basex.core.cmd.XQuery;
import org.basex.query.QueryException;
import org.basex.query.QueryProcessor;
import org.basex.query.iter.Iter;
import org.basex.query.value.item.Item;

/**
 *
 * @author Usuario
 */
public class GestionBD {

    private static final String directorioBD = "src/recursos/taller.xml";
    private static Context contexto;
    private static CreateDB bd;

    /**
     * Método que conecta con la base de datos y devuelve un mensaje informando
     * del resultado de la acción.
     *
     * @return String
     */
    public static String conectarBD() {
        String msj;

        contexto = new Context();
        try {
            bd = new CreateDB("Taller", directorioBD);
            bd.execute(contexto);
            msj = "Conexión exitosa con BD: " + directorioBD;
        } catch (BaseXException e) {
            msj = "Error al conectar con la base de datos: " + e.getMessage();
        }
        return msj;
    }

    /**
     * Método que desconecta de la base de datos y devuelve un mensaje
     * informando del resultado de la acción.
     *
     * @return String
     */
    public static String desconectarBD() {
        String msj;

        try {
            contexto.close();
            msj = "Se ha realizado la desconexión de la BD: " + directorioBD;
        } catch (Exception e) {
            msj = "Error al desconectar con la base de datos: " + e.getMessage();
        }
        return msj;
    }

    /**
     * Método que prepara el directorio AD52425 para almacenar las colecciones
     * de Vehículos, Entradas y Marcas e invoca el método que las crea con su
     * correspondiente consulta y el nombre de los ficheros que se deben
     * generar. Devuelve true en caso de que todo haya ido bien o false en caso
     * contrario.
     *
     * @return
     */
    public static boolean extraeColecciones() {
        Boolean res;

        try {
            // Se prepara el directorio para ubicar las coleeciones
            File dir = new File("AD52425");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Se invoca a extraeColeccion para cada una de las colecciones con su consulta adecuada y
            // se almacena en res el resultado (acumulado) de las misma para controlar si se han producido errores
            res = extraeColeccion("for $x in //vehiculos/vehiculo return $x", "AD52425/vehiculo");
            res = res && extraeColeccion("for $x in //reparaciones/entrada return $x", "AD52425/reparacion");
            res = res && extraeColeccion("for $x in //marca return $x", "AD52425/marca");
        } catch (SecurityException e) {
            System.out.println("Error: " + e.getMessage());
            res = false;
        }
        return res;
    }

    /**
     * Método que recibe como primer parámetro una consulta que devuelve una
     * serie de nodos y genera para cada nodo un fichero .xml, donde lo
     * almacena. Los nombres y ubicación de los ficheros que se van a generar lo
     * indica el segundo parámetro al que se le va añadiendo el número de nodo y
     * ese será el nombre final de cada fichero. Devuelve true en caso de que
     * todo haya ido bien o false en caso contrario.
     *
     * @param query
     * @param ruta
     * @return
     */
    public static boolean extraeColeccion(String query, String ruta) {
        Boolean res = true;

        try (QueryProcessor proc = new QueryProcessor(query, contexto)) {
            Iter iter = proc.iter();
            Item item;
            int contador = 1;
            while ((item = iter.next()) != null) {
                try (FileWriter fw = new FileWriter(ruta + contador + ".xml")) {
                    fw.write(item.serialize().toString());
                } catch (IOException e) {
                    System.out.println("Error archivos: " + e.getMessage());
                    res = false;
                }
                contador++;
            }
        } catch (QueryException e) {
            System.out.println("Error consulta: " + e.getMessage());
            res = false;
        }
        return res;
    }

    /**
     * Método que recupera la consulta contenida en el archivo cuyo nombre se le
     * pasa como parámetro y la ejecuta Devuelve es String resultado de la
     * consulta
     *
     * @param nombreArchivo
     * @return
     */
    public static String ejecutarConsultaDeArchivo(String nombreArchivo) {
        String resultado = "";
        String query = "";
        String ruta = "src/consultas/" + nombreArchivo;
        String lineaScript;

        // Leer el archivo para obtener la consulta
        try (BufferedReader br = new BufferedReader(new FileReader(new File(ruta)))) {
            do {
                lineaScript = br.readLine();
                if (lineaScript != null) {
                    query += lineaScript + " ";
                }
            } while (lineaScript != null);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        // Ejercutar la consulta 
        try {
            XQuery xq = new XQuery(query);
            resultado = xq.execute(contexto);
        } catch (BaseXException e) {
            System.out.println("Error BD " + e.getMessage());
        }
        return resultado;
    }

    public static String mostrarTodo() {
        String resultado = "";
        try {
            XQuery xq = new XQuery("for $x in /taller return $x");
            resultado = xq.execute(contexto);
        } catch (BaseXException e) {
            System.out.println("Error BD " + e.getMessage());
        }
        return resultado;
    }

}
