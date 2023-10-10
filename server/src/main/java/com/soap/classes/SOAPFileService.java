package com.soap.classes;

import com.soap.interfaces.ISOAPFileService;
import interfaces.RMIServiceAdapter;
import jakarta.jws.WebService;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@WebService(endpointInterface = "com.soap.interfaces.ISOAPFileService")
public class SOAPFileService implements ISOAPFileService {
	private static final String BASE_DIRECTORY = "C:\\Users\\USER\\Documents\\Universidad\\LastSemester\\Distribuidos\\Proyecto de Aula\\ArchivosSOAP\\";
	private ArrayList<File> files = new ArrayList<>();

	// Estructura para mapeo de usuarios a carpetas
	private Map<String, String> userToFolderMap = new HashMap<>();

	// Método para obtener la carpeta de un usuario o crear una nueva si no existe
	private String getUserFolder(String user) {
		return userToFolderMap.computeIfAbsent(user, key -> {
			String folderId = generateUniqueFolderId(); // Implementa esto para generar un ID único
			createDirectory(user, folderId); // Crea la carpeta en el servidor SOAP
			return folderId;
		});
	}

	private String generateUniqueFolderId(){
		String uniqueId = UUID.randomUUID().toString();
		return uniqueId;
	}

	// Método para obtener la carpeta de un usuario para subcarpetas
	private String getUserSubFolder(String user, String parentFolder) {
		String parentFolderId = getUserFolder(user);
		return parentFolderId + "/" + generateUniqueSubFolderId(); // Implementacion ID unico
	}

	private String generateUniqueSubFolderId() {
		// Generar un UUID aleatorio como ID único para la subcarpeta
		String uniqueId = UUID.randomUUID().toString();
		return uniqueId;
	}


	@Override
	public void createDirectory(String user, String path) {
		String userFolder = getUserFolder(user);
		String fullPath = BASE_DIRECTORY + userFolder + "/" + path;

			try {
				RMIServiceAdapter rmiAdapter = new RMIServiceAdapterImpl();
				rmiAdapter.createDirectory(userFolder, path);
			} catch (RemoteException e) {
				e.printStackTrace();
				System.err.println("Error al cargar una copia del archivo en el nodo RMI.");
			}
	}


	@Override
	public ArrayList<String> listDirectories(String folderID) {
		ArrayList<String> directoryNames = new ArrayList<>();
		try {
			RMIServiceAdapter rmiAdapter = new RMIServiceAdapterImpl();
			directoryNames = rmiAdapter.listDirectories(folderID);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println("Error al cargar una copia del archivo en el nodo RMI.");
		}

		return directoryNames;
	}


	@Override
	public void uploadFile(String folderID, String fileName, byte[] fileData) {
		try {
			RMIServiceAdapter rmiAdapter = new RMIServiceAdapterImpl();
			rmiAdapter.uploadFileToNode(folderID, fileName, fileData);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println("Error al cargar una copia del archivo en el nodo RMI.");
		}

	}



	@Override
	public byte[] downloadFile(String fileID) {
		byte[] file = new byte[0];

		try {
			RMIServiceAdapter rmiAdapter = new RMIServiceAdapterImpl();
			file = rmiAdapter.downloadFile(fileID);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.err.println("Error al cargar una copia del archivo en el nodo RMI.");
		}
		return file;
	}

	@Override
	public void moveFile(String fileID, String folderID, String newFolderID) {
		try {
			RMIServiceAdapter rmiAdapter = new RMIServiceAdapterImpl();
			rmiAdapter.moveFile(fileID, folderID, newFolderID);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error inesperado al mover el archivo.");
		}
	}

	@Override
	public void renameFile(String fileID, String newName) {
		try {
			RMIServiceAdapter rmiAdapter = new RMIServiceAdapterImpl();
			rmiAdapter.renameFile(fileID, newName);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error inesperado al renombrar el archivo.");
		}
	}

	@Override
	public ArrayList<String> listFilesInDirectory(String folderID) {
		ArrayList<String> fileNames = new ArrayList<>();
		try {
			RMIServiceAdapter rmiAdapter = new RMIServiceAdapterImpl();
			fileNames = rmiAdapter.listFilesInDirectory(folderID);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error inesperado al renombrar el archivo.");
		}
		return fileNames;
	}

	@Override
	public void deleteFile(String fileID) {
		try {
			RMIServiceAdapter rmiAdapter = new RMIServiceAdapterImpl();
			rmiAdapter.deleteFile(fileID);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error inesperado al renombrar el archivo.");
		}
	}

	private String getFileExtension(String fileName) {
		int lastDotIndex = fileName.lastIndexOf(".");
		if (lastDotIndex > 0) {
			return fileName.substring(lastDotIndex + 1);
		}
		return null;
	}






	@Override
	public void shareFile(String user, String path, String recipient) {
		// Aquí implementa la lógica para compartir un archivo con un usuario específico
		String userFolder = getUserFolder(user);
		File fileToShare = new File(BASE_DIRECTORY + userFolder + "/" + path);
		if (fileToShare.exists()) {
			// Agrega la lógica para gestionar la compartición con el usuario
			System.out.println("Archivo compartido con éxito con " + recipient);
		} else {
			System.err.println("El archivo no existe.");
		}
	}


}
