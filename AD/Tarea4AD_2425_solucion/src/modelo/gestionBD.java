/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;


import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.ext.IncompatibleFileFormatException;
import com.db4o.ext.OldFormatException;
import com.db4o.query.Query;

/**
 *
 * @author Usuario
 */
public class gestionBD {
    
    private static String nombreBD = "musica.db4";
    private static ObjectContainer bd; 
    
    /**
     * Método que conecta con la base de datos y devulve un mensaje informando del resultado de la acción.
     * @return String
     */
    public static String conectarBD (){        
       String msj="";
       
       try {
           bd = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), nombreBD);
           msj= "Conexión exitosa con BD: "+ nombreBD;
       }
       catch (Db4oIOException | DatabaseFileLockedException | IncompatibleFileFormatException | OldFormatException | DatabaseReadOnlyException e){
           msj="Error al conectar con la base de datos: " + e.getMessage();
       }
       return msj;  
    }
    
    /**
     * Método que desconecta de la base de datos y devulve un mensaje informando del resultado de la acción.
     * @return String
     */
    public static String desconectarBD (){        
       String msj="";
       
       try {
           bd.close();
           msj="Se ha realizado la desconexión de la BD: "+ nombreBD;
       }
       catch (Db4oIOException e){
           msj="Error al desconectar con la base de datos: " + e.getMessage();
       }
       return msj;   
    }
    
    /**
     * Metodo que comprueba que no existe un autor con el nombre del parámetro pnombre y almacena el autor con los datos
     * de los parámetros recibidos. Devuelve un mensaje informando del resultado de la acción.
     * @param pnombre
     * @param pnacionalidad
     * @param ppaisN
     * @param pañoN
     * @param pingresosA
     * @return String
     */
    public static String añadirAutor (String pnombre, String pnacionalidad, String ppaisN, Integer pañoN, Double pingresosA){
        Autor a; 
        ObjectSet res;
        String msj;
        
        try{ // comprueba que no hay ningún autor con ese nombre //
            a = new Autor(pnombre, null, null, 0 , 0);
            res =bd.queryByExample(a);
            if (!res.isEmpty()) msj="Ya existe una autor con ese nombre";
            else { // añade el nuevo autor //
                a = new Autor (pnombre, pnacionalidad, ppaisN, pañoN, pingresosA);
                bd.store(a);
                bd.commit();
                msj="Autor añadido correctamente a BD";
            }
        }
        catch (Db4oIOException | DatabaseClosedException | DatabaseReadOnlyException e){
            msj="Error BD: "+ e.getMessage();
            bd.rollback();
        }
        return msj;
    }
    
    /**
     * Métódo que devuelve un conjunto de objetos con todos los autores de la base de datos
     * @return 
     */
    public static ObjectSet obtenerTodosAutores(){
        Autor a;
        ObjectSet res=null;
        
        try{ // obtiene todos los autores de la BD //
            a = new Autor (null, null, null, 0, 0);
            res =bd.queryByExample(a);         
        }
        catch (Db4oIOException | DatabaseClosedException e){
            System.out.println("Error BD: "+ e.getMessage());
        }
        return res;
    }
    
    /**
     * Método que comprueba que no existe una canción con el mismo identificador que el parámetro pidentificador
     * y almacena la canción con los datos de los parámetros recibidos. Devuelve un mensaje informando del 
     * resultado de la acción.
     * @param pidentificador
     * @param ptitulo
     * @param pduracion
     * @param pañoC
     * @param pgenero
     * @param pautor
     * @return 
     */
    public static String añadirCancion (String pidentificador, String ptitulo, Double pduracion, Integer pañoC, String pgenero, String pautor){
        Autor a,autor; 
        Cancion c, nuevaCancion;
        ObjectSet res;
        String msj;
        
        try{ // comprueba que no hay ninguna canción con ese identificador //
            c = new Cancion(pidentificador, null, 0 , 0, null);
            res =bd.queryByExample(c);
            if (!res.isEmpty()) msj="Ya existe una canción con ese identificador";
            else { 
                // recupero el autor con el nombre indicado //
                a = new Autor (pautor, null, null, 0, 0);
                res=bd.queryByExample(a);
                // creo la canción indicada con su autor y la almaceno en BD
                nuevaCancion = new Cancion (pidentificador, ptitulo, pduracion, pañoC, pgenero);
                autor = (Autor)res.next();
                nuevaCancion.setAutor(autor);
                bd.store(nuevaCancion);
                bd.commit();
                msj="Canción añadida correctamente a BD";
            }
        }
        catch (Db4oIOException | DatabaseClosedException | DatabaseReadOnlyException e){
            msj="Error BD: "+ e.getMessage();
            bd.rollback();
        }
        return msj;
    }
    
    /**
     * Métódo que devuelve un conjunto de objetos con todas las canciones de la base de datos
     * @return 
     */
    public static ObjectSet obtenerTodasCanciones(){
        Cancion c;
        ObjectSet res=null;
       
        try{ // obtiene todas las canciones de la BD //
            c = new Cancion (null, null, 0, 0, null);
            res =bd.queryByExample(c);         
        }
        catch (Db4oIOException | DatabaseClosedException e){
            System.out.println("Error BD: "+ e.getMessage());
        }
        return res;
    }
    /**
     * Método que recibe como parámetro el identificador de una canción que está en la base de datos y borra la 
     * canción, devolviendo un mendaje informativo del resultado de la acción
     * @param pidentificador
     * @return 
     */
    public static String borrarCancion (String pidentificador){
        Cancion c;
        ObjectSet res;
        String msj;
        
        try{ // recupera la canción con ese identificador y la borra //
            c = new Cancion(pidentificador, null, 0 , 0, null);
            res =bd.queryByExample(c);
            bd.delete(res.next());
            bd.commit();
            msj="Canción eliminada correctamente de la BD";
        }
        catch (Db4oIOException | DatabaseClosedException e){
            msj="Error BD: "+ e.getMessage();
            bd.rollback();
        }
        return msj;
    }
    
    /**
     * Método que recibe como parámetro el nombre de un autor que está en la base de datos y lo borra (pero sólo
     * en el caso de que no tenga canciones en la base de datos). Devuelve mensaje informativo del resultado de
     * la acción.
     * @param pNombre
     * @return 
     */
    public static String borrarAutor (String pNombre){
        Cancion c;
        Autor a, autor;
        ObjectSet resA, resC;
        String msj;
        
        try{ // recupera el autor de la BD con ese nombre//
            a = new Autor(pNombre, null, null, 0, 0 );
            resA = bd.queryByExample(a);
            autor = (Autor)resA.next();
            // recupera las canciones de ese autor
            c = new Cancion (null, null, 0, 0, null);
            c.setAutor(autor);
            resC =bd.queryByExample(c);
            if (resC.hasNext()) //si el autor tiene canciones no se puede borrar
                msj="No es posible eliminar a ese autor porque tiene canciones en la BD";
            else{ //si el autor no tiene canciones se borra
                bd.delete(autor);
                bd.commit();
                msj="Autor eliminado correctamente de la BD";
            }
        }
        catch (Db4oIOException | DatabaseClosedException e){
            msj="Error BD: "+ e.getMessage();
            bd.rollback();
        }
        return msj;
    }
    
    /**
     * Métódo que devuelve un conjunto de objetos con todas las canciones de la BD ordenadas por su campo título
     * @return 
     */
    public static ObjectSet obtenerTodasCancionesOrdenadasTitulo(){
        ObjectSet res=null;
       
        try{ // obtiene todas las canciones ordenadas por el campo título //
            Query query = bd.query();
            query.constrain(Cancion.class);
            query.descend("titulo").orderAscending();
            res= query.execute();
        }
        catch (DatabaseClosedException e){
            System.out.println("Error BD: "+ e.getMessage());
        }
        return res;
    }
    
    /**
     * Método que recibe cómo parámetro el nombre un autor y busca todas las canciones de ese autor
     * @param pNombre
     * @return 
     */
    public static ObjectSet obtenerCancionesDeAutor(String pNombre){
        ObjectSet resC=null;
                
        try{ // recupera por plantilla todas las canciones del autor con nombre pNombre
            Autor a = new Autor(pNombre, null, null, 0, 0 );
            Cancion c = new Cancion (null, null, 0, 0, null);
            c.setAutor(a);
            resC =bd.queryByExample(c);
        }
        catch (Db4oIOException | DatabaseClosedException e){
            System.out.println("Error BD: "+ e.getMessage());
        }
        return resC;
    }
    
    /**
     * Método que recibe cómo parámetro una nacionalidad y búsca en la base de datos todas las canciones
     * cuyo autor tenga esa nacionalidad
     * @param pNacionalidad
     * @return 
     */
    public static ObjectSet obtenerCancionesPorNacionalidadAutor(String pNacionalidad){
        ObjectSet  resC=null;
                
        try{ // recupera por plantilla todas las canciones cuyo autor sea de la nacionalidad pNacionalidad//
            Autor a = new Autor(null, pNacionalidad, null, 0, 0 );
            Cancion c = new Cancion (null, null, 0, 0, null);
            c.setAutor(a);
            resC =bd.queryByExample(c);
        }
        catch (Db4oIOException | DatabaseClosedException e){
            System.out.println("Error BD: "+ e.getMessage());
        }
        return resC;
    }
    
    /**
     * Métódo que recibe como parámetros dos valores pIngresoMin y pIngresoMax y devuelve un conjunto de 
     * objetos con todos los autores que sus ingresos estén comprendidos entre esos dos valores
     * @return 
     */
    public static ObjectSet obtenerAutoresPorIngresos(Double pIngresoMin, Double pIngresoMax){
        ObjectSet res=null;
       
        try{ // obtiene todas las autores con ingresos mayores que pIngresoMin y menores que pIngresoMax //
            Query query = bd.query();
            query.constrain(Autor.class);
            query.descend("ingresos_anuales").constrain(pIngresoMin).greater();
            query.descend("ingresos_anuales").constrain(pIngresoMax).smaller();
            res= query.execute();
        }
        catch (DatabaseClosedException e){
            System.out.println("Error BD: "+ e.getMessage());
        }
        return res;
    }
    
    /**
     * Método que incrementa en un 5% los ingresos de todos los autores y devuelve mensaje con el resultado de
     * la acción
     * @return 
     */
    public static String aumentarIngresosAutores(){
        String msj=null;
       
        try{ // obtiene todas los autoes de la base de datos //
            Query query = bd.query();
            query.constrain(Autor.class);
            ObjectSet res= query.execute();
            
            while (res.hasNext()){ //recorre los autores incrementando sus ingresos
                Autor a = (Autor)res.next();
                a.setIngresos_anuales(a.getIngresos_anuales()*1.05);
                bd.store(a);
                bd.commit();
                msj="Se ha incrementado en un 5% los ingresos de los autores";
            }               
        }
        catch (DatabaseReadOnlyException | Db4oIOException | DatabaseClosedException e){
            msj="Error BD: "+ e.getMessage();
            bd.rollback();
        }
        return msj;
    }
    
}
