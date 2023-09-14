package com.soap.interfaces;

import java.util.ArrayList;
import com.soap.classes.Cancion;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface ISOAPCancion {
    @WebMethod
    public ArrayList<Cancion> buscarCancionesGenero(String genero);

    @WebMethod
	public Cancion buscarCancionTitulo(String nombre);

    @WebMethod
	public ArrayList<String> obtenerGeneros();

    @WebMethod
	public ArrayList<Cancion>  obtenerCanciones() ;
}
