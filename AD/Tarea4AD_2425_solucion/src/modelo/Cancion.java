/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Usuario
 */
public class Cancion {
    
    private String identificador;
    private String titulo;
    private double duracion;
    private int anio_creacion;
    private String genero;
    private Autor autor;

    public Cancion() {
    }

    
    public Cancion(String identificador, String titulo, double duracion, int anio_creacion, String genero) {
        this.identificador = identificador;
        this.titulo = titulo;
        this.duracion = duracion;
        this.anio_creacion = anio_creacion;
        this.genero = genero;
    }
   

    /**
     * Get the value of identificador
     *
     * @return the value of identificador
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Set the value of identificador
     *
     * @param identificador new value of identificador
     */
    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }
    
    /**
     * Get the value of titulo
     *
     * @return the value of titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Set the value of titulo
     *
     * @param titulo new value of titulo
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    /**
     * Get the value of duracion
     *
     * @return the value of duracion
     */
    public double getDuracion() {
        return duracion;
    }

    /**
     * Set the value of duracion
     *
     * @param duracion new value of duracion
     */
    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }
    
    /**
     * Get the value of anio_creacion
     *
     * @return the value of anio_creacion
     */
    public int getAnio_creacion() {
        return anio_creacion;
    }

    /**
     * Set the value of anio_creacion
     *
     * @param anio_creacion new value of anio_creacion
     */
    public void setAnio_creacion(int anio_creacion) {
        this.anio_creacion = anio_creacion;
    }
    
    /**
     * Get the value of genero
     *
     * @return the value of genero
     */
    public String getGenero() {
        return genero;
    }

    /**
     * Set the value of genero
     *
     * @param genero new value of genero
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }
    
    /**
     * Get the value of autor
     *
     * @return the value of autor
     */
    public Autor getAutor() {
        return autor;
    }

    /**
     * Set the value of autor
     *
     * @param autor new value of autor
     */
    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "Cancion{" + "identificador=" + identificador + ", titulo=" + titulo + ", duracion=" + duracion + ", anio_creacion=" + anio_creacion + ", genero=" + genero + ", autor=" + autor + '}';
    }

    
    
    

}
