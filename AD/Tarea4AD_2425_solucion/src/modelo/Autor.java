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
public class Autor {
    
    private String nombre;
    
    private String nacionalidad;
    
    private String pais_residencia;

    private int año_nacimiento;
    
    private double ingresos_anuales;

    public Autor() {
    }

    public Autor(String nombre, String nacionalidad, String pais_residencia, int año_nacimiento, double ingresos_anuales) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.pais_residencia = pais_residencia;
        this.año_nacimiento = año_nacimiento;
        this.ingresos_anuales = ingresos_anuales;
    }

    
    /**
     * Get the value of nombre
     *
     * @return the value of nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Set the value of nombre
     *
     * @param nombre new value of nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Get the value of nacionalidad
     *
     * @return the value of nacionalidad
     */
    public String getNacionalidad() {
        return nacionalidad;
    }

    /**
     * Set the value of nacionalidad
     *
     * @param nacionalidad new value of nacionalidad
     */
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
    
    /**
     * Get the value of pais_residencia
     *
     * @return the value of pais_residencia
     */
    public String getPais_residencia() {
        return pais_residencia;
    }

    /**
     * Set the value of pais_residencia
     *
     * @param pais_residencia new value of pais_residencia
     */
    public void setPais_residencia(String pais_residencia) {
        this.pais_residencia = pais_residencia;
    }
    
    /**
     * Get the value of año_nacimiento
     *
     * @return the value of año_nacimiento
     */
    public int getAño_nacimiento() {
        return año_nacimiento;
    }

    /**
     * Set the value of año_nacimiento
     *
     * @param año_nacimiento new value of año_nacimiento
     */
    public void setAño_nacimiento(int año_nacimiento) {
        this.año_nacimiento = año_nacimiento;
    }

    /**
     * Get the value of ingresos_anuales
     *
     * @return the value of ingresos_anuales
     */
    public double getIngresos_anuales() {
        return ingresos_anuales;
    }

    /**
     * Set the value of ingresos_anuales
     *
     * @param ingresos_anuales new value of ingresos_anuales
     */
    public void setIngresos_anuales(double ingresos_anuales) {
        this.ingresos_anuales = ingresos_anuales;
    }

    @Override
    public String toString() {
        return "Autor{" + "nombre=" + nombre + ", nacionalidad=" + nacionalidad + ", pais_residencia=" + pais_residencia + ", a\u00f1o_nacimiento=" + año_nacimiento + ", ingresos_anuales=" + ingresos_anuales + '}';
    }
    
    
}
