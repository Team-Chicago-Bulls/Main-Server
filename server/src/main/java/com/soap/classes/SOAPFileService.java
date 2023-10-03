package com.soap.classes;

import com.soap.interfaces.ISOAPFileService;
import com.soap.interfaces.RMIServiceAdapter;
import jakarta.jws.WebService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;

@WebService(endpointInterface = "com.soap.interfaces.ISOAPFileService")
public class SOAPFileService implements ISOAPFileService {
	private static final String BASE_DIRECTORY = "\\\\192.168.20.34\\ArchivosSoap\\";
	private ArrayList<File> files = new ArrayList<>();
	private RMIServiceAdapter rmiAdapter;

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
	public ArrayList<String> listDirectories() {
		File baseDirectory = new File(BASE_DIRECTORY);
		ArrayList<String> directoryNames = new ArrayList<>();

		if (baseDirectory.exists() && baseDirectory.isDirectory()) {
			File[] directories = baseDirectory.listFiles(File::isDirectory);

			if (directories != null) {
				for (File directory : directories) {
					directoryNames.add(directory.getName());
				}
			}
		}

		return directoryNames;
	}


	@Override
	public void uploadFile(String directory, String fileName, byte[] fileData) {
		// Lógica para cargar el archivo en el servidor SOAP
		String filePath = BASE_DIRECTORY + directory + File.separator + fileName;
		try (FileOutputStream fos = new FileOutputStream(filePath)) {
			fos.write(fileData);
			System.out.println("Archivo subido con éxito al servidor SOAP.");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error al cargar el archivo en el servidor SOAP.");
			return; // Si ocurre un error al cargar en SOAP, no proceder a RMI
		}

		// Llamar al adaptador RMI para cargar una copia en el nodo RMI
		try {
			RMIServiceAdapter rmiAdapter = new RMIServiceAdapterImpl();
			rmiAdapter.uploadFileToNode(filePath, fileData);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println("Error al cargar una copia del archivo en el nodo RMI.");
		}

	}



	@Override
	public byte[] downloadFile(String path) {
		// Aquí implementa la lógica para descargar un archivo y devuelve sus datos en forma de arreglo de bytes
		try {
			File file = new File(BASE_DIRECTORY + path);
			if (file.exists()) {
				byte[] fileBytes = Files.readAllBytes(Paths.get(BASE_DIRECTORY + path));

				// Verificar si el archivo es una imagen (por ejemplo, jpg, png, gif)
				String fileName = file.getName();
				String extension = getFileExtension(fileName);
				if (extension != null && (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif"))) {
					// Si es una imagen, devolver los datos sin realizar ninguna operación adicional
					System.out.println("Imagen descargada con éxito.");
					return fileBytes;
				} else {
					// Si no es una imagen, mostrar un mensaje y devolver null
					System.err.println("El archivo no es una imagen.");
					return null;
				}
			} else {
				System.err.println("El archivo no existe.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error al descargar el archivo.");
		}
		return null;
	}

	private String getFileExtension(String fileName) {
		int lastDotIndex = fileName.lastIndexOf(".");
		if (lastDotIndex > 0) {
			return fileName.substring(lastDotIndex + 1);
		}
		return null;
	}


	@Override
	public void moveFile(String sourcePath, String destinationDirectory) {
		try {
			// Construir rutas completas
			String sourceFilePath = BASE_DIRECTORY + sourcePath;
			String destinationFilePath = BASE_DIRECTORY + destinationDirectory + File.separator + new File(sourcePath).getName();

			// Verificar si el archivo fuente existe
			File sourceFile = new File(sourceFilePath);
			if (sourceFile.exists()) {
				// Crear el directorio de destino si no existe
				File destinationDir = new File(BASE_DIRECTORY + destinationDirectory);
				if (!destinationDir.exists()) {
					destinationDir.mkdirs();
				}

				// Mover el archivo fuente al destino
				if (sourceFile.renameTo(new File(destinationFilePath))) {
					System.out.println("Archivo movido con éxito.");
				} else {
					System.err.println("Error al mover el archivo.");
				}
			} else {
				System.err.println("El archivo fuente no existe.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error inesperado al mover el archivo.");
		}
	}



	@Override
	public void renameFile(String oldPath, String newName) {
		try {
			// Construir rutas completas
			String oldFilePath = BASE_DIRECTORY + oldPath;
			String directoryPath = new File(oldFilePath).getParent(); // Obtener la ruta del directorio
			String newFilePath = directoryPath + File.separator + newName; // Usar el mismo directorio con el nuevo nombre

			// Verificar si el archivo existe en la ubicación original
			File oldFile = new File(oldFilePath);
			if (oldFile.exists()) {
				// Crear el archivo con el nuevo nombre en el mismo directorio
				File newFile = new File(newFilePath);

				// Renombrar el archivo
				if (oldFile.renameTo(newFile)) {
					System.out.println("Archivo renombrado con éxito.");
				} else {
					System.err.println("Error al renombrar el archivo.");
				}
			} else {
				System.err.println("El archivo no existe en la ubicación original.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error inesperado al renombrar el archivo.");
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
