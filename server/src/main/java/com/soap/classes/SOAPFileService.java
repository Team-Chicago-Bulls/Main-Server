package com.soap.classes;

import com.soap.interfaces.ISOAPFileService;

import jakarta.jws.WebService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

@WebService(endpointInterface = "com.soap.interfaces.ISOAPFileService")
public class SOAPFileService implements ISOAPFileService {
	private static final String BASE_DIRECTORY = "/ruta/del/directorio/base";
	private ArrayList<File> files = new ArrayList<>();

	@Override
	public void createDirectory(String path) {
		// Combinar la ruta base con la ruta proporcionada
		String fullPath = BASE_DIRECTORY + path;

		// Crear un objeto File para el directorio
		File directory = new File(fullPath);

		// Verificar si el directorio ya existe
		if (directory.exists()) {
			System.out.println("El directorio ya existe.");
		} else {
			// Intentar crear el directorio
			if (directory.mkdirs()) {
				System.out.println("Directorio creado con éxito.");
			} else {
				System.err.println("Error al crear el directorio.");
			}
		}
	}

	@Override
	public void uploadFile(String directory, byte[] fileData) {
		// Aquí implementa la lógica para cargar un archivo en el directorio especificado
		String filePath = BASE_DIRECTORY + directory + File.separator + "nombre_del_archivo.ext"; // Cambia "nombre_del_archivo.ext" al nombre real
		try (FileOutputStream fos = new FileOutputStream(filePath)) {
			fos.write(fileData);
			System.out.println("Archivo cargado con éxito.");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error al cargar el archivo.");
		}
	}

	@Override
	public byte[] downloadFile(String path) {
		// Aquí implementa la lógica para descargar un archivo y devuelve sus datos en forma de arreglo de bytes
		try {
			File file = new File(BASE_DIRECTORY + path);
			if (file.exists()) {
				byte[] fileBytes = Files.readAllBytes(Paths.get(BASE_DIRECTORY + path));
				System.out.println("Archivo descargado con éxito.");
				return fileBytes;
			} else {
				System.err.println("El archivo no existe.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error al descargar el archivo.");
		}
		return null;
	}

	@Override
	public void moveOrRenameFile(String oldPath, String newPath) {
		// Aquí implementa la lógica para mover o renombrar un archivo de la ruta antigua a la nueva
		File oldFile = new File(BASE_DIRECTORY + oldPath);
		File newFile = new File(BASE_DIRECTORY + newPath);
		if (oldFile.exists()) {
			if (oldFile.renameTo(newFile)) {
				System.out.println("Archivo movido o renombrado con éxito.");
			} else {
				System.err.println("Error al mover o renombrar el archivo.");
			}
		} else {
			System.err.println("El archivo no existe en la ubicación original.");
		}
	}

	@Override
	public void deleteFile(String path) {
		// Aquí implementa la lógica para eliminar un archivo
		File fileToDelete = new File(BASE_DIRECTORY + path);
		if (fileToDelete.exists()) {
			if (fileToDelete.delete()) {
				System.out.println("Archivo eliminado con éxito.");
			} else {
				System.err.println("Error al eliminar el archivo.");
			}
		} else {
			System.err.println("El archivo no existe.");
		}
	}

	@Override
	public void shareFile(String path, String user) {
		// Aquí implementa la lógica para compartir un archivo con un usuario específico
		File fileToShare = new File(BASE_DIRECTORY + path);
		if (fileToShare.exists()) {
			// Agrega la lógica para gestionar la compartición con el usuario
			System.out.println("Archivo compartido con éxito con " + user);
		} else {
			System.err.println("El archivo no existe.");
		}
	}

	@Override
	public ArrayList<String> listFilesInDirectory(String path) {
		// Aquí implementa la lógica para listar los archivos en un directorio y devuelve sus nombres
		ArrayList<String> fileNames = new ArrayList<>();
		File directory = new File(BASE_DIRECTORY + path);
		if (directory.exists() && directory.isDirectory()) {
			File[] files = directory.listFiles();
			if (files != null) {
				for (File file : files) {
					fileNames.add(file.getName());
				}
				System.out.println("Archivos listados con éxito.");
			} else {
				System.err.println("Error al listar archivos.");
			}
		} else {
			System.err.println("El directorio no existe o no es válido.");
		}
		return fileNames;
	}
}
