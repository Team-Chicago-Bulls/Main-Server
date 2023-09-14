package com.soap.classes;

import jakarta.jws.WebService;

import java.util.ArrayList;

import com.soap.interfaces.ISOAPCancion;

@WebService(endpointInterface = "com.soap.interfaces.ISOAPCancion")
public class SOAPCancion implements ISOAPCancion {

     private ArrayList<Cancion> canciones;  

    public SOAPCancion() {
        this.canciones = new ArrayList<Cancion>();
		this.canciones.add(new Cancion("Enemy", 2021, "Rock Alternativo", 204));
		this.canciones.add(new Cancion("Smooth Criminal", 1987, "Pop", 251));
		this.canciones.add(new Cancion("Flaca", 1997, "Rock", 238));
		this.canciones.add(new Cancion("Bones", 2022, "Rock Alternativo", 160));
		
    }

    @Override
	public ArrayList<Cancion> buscarCancionesGenero(String genero)  {
		ArrayList<Cancion> canciones = new ArrayList<Cancion>();
		for (Cancion song : this.canciones) {
            if (song.getGenero().equals(genero)) {
                canciones.add(song);
            }
        }
		return canciones;
	}

	@Override
	public Cancion buscarCancionTitulo(String nombre)  {
		for (Cancion song : this.canciones) {
            if (song.getNombre().equals(nombre)) {
                return song;
            }
        }
		return null;
	}

	@Override
	public ArrayList<String> obtenerGeneros() {
		ArrayList<String> generos = new ArrayList<String>();
		for (Cancion song : this.canciones) {
            generos.add(song.getGenero());
        }
		return generos;
	}

	@Override
	public ArrayList<Cancion> obtenerCanciones()  {
		return this.canciones;
	}
    
}
