package com.soap.classes;

import java.io.Serializable;

public class Cancion implements Serializable {
    
	private static final long serialVersionUID = 1L;
	private String nombre;
    private int year;
    private String genero;
    private int tiempo;

    public Cancion(String nombre, int year, String genero, int tiempo) {
        this.nombre = nombre;
        this.year = year;
        this.genero = genero;
        this.tiempo = tiempo;
    }
    
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public String getGenero() {
        return genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getTiempo() {
        return tiempo;
    }
    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public String toString() {
        return "Cancion [nombre=" + nombre + ", year=" + year + ", genero=" + genero + ", tiempo=" + tiempo + "] \n";
    }
    
    
    

}
