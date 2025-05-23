package crearbeans_ad6;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.beans.*;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventListener;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class ProductoBean implements Serializable {
    
    
    private PropertyChangeSupport propertySupport;

    protected String referencia;
    protected String nombre;
    protected String descripcion;
    protected float precio;
    protected float descuento;

    public ProductoBean() {
        propertySupport = new PropertyChangeSupport(this);
        try {

            recargarFilas();
        } catch (ClassNotFoundException ex) {
            this.referencia = "";
            this.nombre = "";
            this.descripcion = "";
            this.precio = 0;
            this.descuento = 0;
            Logger.getLogger(ProductoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Establece la referencia según el valor del parámetro
     * @param referencia 
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
    
    /**
     * Establece el nombre según el valor del parámetro
     * @param nombre 
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece la descripción según el valor del parámetro
     * @param descripcion 
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Establece el precio según el valor del parámetro
     * @param precio 
     */
    public void setPrecio(float precio) {
        this.precio = precio;
    }

    /**
     * Establece el descuento según el valor del parámetro
     * @param descuento 
     */
    public void setDescuento(float descuento) {
        this.descuento = descuento;
    }
    
    /**
     * Devuelve el valor de referencia
     * @return String
     */
    public String getReferencia() {
        return referencia;
    }
    
    /**
     * Devuelve el valor de nombre
     * @return String
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve el valor de la descripción
     * @return String
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Devuelve el valor de precio
     * @return float
     */
    public float getPrecio() {
        return precio;
    }
    
    /**
     * Devuelve el valor de descuento
     * @return float
     */
    public float getDescuento() {
        return descuento;
    }
    
    /**
     * Clase interna Producto para manejar objetos con los mismos campos que los campos que tiene un registro
     * producto en las tablas de MySQL, y así trabajar más comodamente con los registros.
     */
    public class Producto {

        public String referencia;
        public String nombre;
        public String descripcion;
        public float precio;
        public float descuento;

        public Producto() {
        }

        public Producto(String referencia, String nombre, String descripcion, float precio, float descuento) {
            this.referencia = referencia;
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.precio = precio;
            this.descuento = descuento;
        }
    }

    
    /**
     * *******************************************************************
     * Cada vez que se modifica el estado de la BD se genera un evento para 
     * que se recargue el componente.
     */
    private BDModificadaListener receptor;

    public class BDModificadaEvent extends java.util.EventObject
    {
        // constructor
        public BDModificadaEvent(Object source)
        {
            super(source);
        }
    }
    
    //Define la interfaz para el nuevo tipo de evento
    public interface BDModificadaListener extends EventListener
    {
        public void capturarBDModificada(BDModificadaEvent ev);
    }
    // Define una clase de evento específica para la acción de añadir producto
    public class AddProductoEvent extends BDModificadaEvent {
        public AddProductoEvent(Object source) {
            super(source);
        }
    }
    // Define una clase de evento específica para la acción de eliminar producto
    public class DeleteProductoEvent extends BDModificadaEvent {
        public DeleteProductoEvent(Object source) {
            super(source);
        }
    }

    public void addBDModificadaListener(BDModificadaListener receptor)
    {
        this.receptor = receptor;
    }
    public void removeBDModificadaListener(BDModificadaListener receptor)
    {
        this.receptor= receptor;
    }

    /**
     * *****************************************************
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }
    
    /**********************************************************/

    /******************************************************
     * Usaremos un vector auxiliar para cargar la información de la
     * tabla de forma que tengamos acceso a los datos sin necesidad
     * de estar conectados constantemente
     */
    private Vector Productos;

    /*******************************************************
     * Actualiza el contenido de la tabla en el vector de productos
     * Las propiedades contienen el valor del primer elementos de la tabla
     */
    private void recargarFilas() throws ClassNotFoundException
    {
        Productos = new Vector();
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/comercio", "root", "admin");
             Statement s = con.createStatement();
             ResultSet rs = s.executeQuery ("select * from productos");)
            {
            while (rs.next())
            {
                Producto p = new Producto(rs.getString("referencia"),
                                          rs.getString("nombre"),
                                          rs.getString("descripcion"),
                                          rs.getFloat("precio"),
                                          rs.getFloat("descuento"));

                Productos.add(p);
            }
            Producto p = new Producto();
            p = (Producto) Productos.elementAt(1);
            this.referencia = p.referencia;
            this.nombre = p.nombre;
            this.descripcion = p.descripcion;
            this.precio = p.precio;
            this.descuento = p.descuento;
            
        } catch (SQLException ex) {
            this.referencia = "";
            this.nombre = "";
            this.descripcion = "";
            this.precio = 0;
            this.descuento = 0;
            Logger.getLogger(ProductoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /***************************************************************************
     *
     * @param i numero de la fila a cargar en las propiedades del componente
     */
    public void seleccionarFila(int i)
    {
        if(i<=Productos.size())
        {
            Producto p;
            p = (Producto) Productos.elementAt(i);
            this.referencia = p.referencia;
            this.nombre = p.nombre;
            this.descripcion = p.descripcion;
            this.precio = p.precio;
            this.descuento = p.descuento;
        }else{
            this.referencia = "";
            this.nombre = "";
            this.descripcion = "";
            this.precio = 0;
            this.descuento = 0;
        }
    }
    
    /***************************************************************************
     * 
     * Devuelve el número de filas cargadas en el vector Productos
     * @return int
     */
    public int numFilas(){    
        return Productos.size();
    }
    
    /***************************************************************************
     * Método que añade un producto a la base de datos.
     * Añade un registro a la base de datos formado a partir
     * de los valores de las propiedades del componente.
     *
     * Se presupone que se han usado los métodos set para configurar
     * adecuadamente las propiedades con los datos del nuevo registro.
     * @throws ClassNotFoundException 
     */
    public void addProducto() throws ClassNotFoundException
    {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/comercio", "root", "admin");
             PreparedStatement s = con.prepareStatement("insert into productos values (?,?,?,?,?)");)
        {    
            s.setString(1, referencia);
            s.setString(2, nombre);
            s.setString(3, descripcion);
            s.setFloat(4, precio);
            s.setFloat(5, descuento);
            s.executeUpdate ();
            recargarFilas();
            receptor.capturarBDModificada( new AddProductoEvent(this));
        }
        catch(SQLException ex)
        {
            Logger.getLogger(ProductoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /***************************************************************************
     * 
     * Comprueba si el producto con la referencia que tiene en ese momento el 
     * componente, existe en la base de datos y devuelve true o false según sea
     * el caso
     * @return boolean
     */
    public boolean existeProducto() {
        String query = "SELECT 1 FROM Productos WHERE referencia=?";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/comercio", "root", "admin");
             PreparedStatement stmt = con.prepareStatement(query)) 
        {
            stmt.setString(1, referencia);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /***************************************************************************
     * Método que borra un producto a la base de datos.
     * Borra un registro de la base de datos formado a partir del valor del campo
     * referenecia que tiene el componente del componente.
     *
     * @throws ClassNotFoundException 
     */
    public void deleteProducto() throws ClassNotFoundException{
        String query = "DELETE FROM Productos WHERE referencia=?";

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/comercio", "root", "admin");
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, referencia);
            stmt.executeUpdate();
            recargarFilas();
            receptor.capturarBDModificada(new DeleteProductoEvent(this));
        } catch (SQLException ex) {
            Logger.getLogger(ProductoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
}
