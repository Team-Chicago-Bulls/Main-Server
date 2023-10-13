package com.soap.classes;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import interfaces.RMIServiceAdapter;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.io.File;

@Component
public class RMIServiceAdapterImpl extends UnicastRemoteObject implements RMIServiceAdapter {
    private static final String BASE_DIRECTORY = "D:\\distro_java\\test_rmi";
    // Estructura para mapeo de usuarios a carpetas
    private Map<String, String> userToFolderMap = new HashMap<>();
    private Map<String, String> fileIDToUserMap = new HashMap<>();
    private RMIServiceAdapter rmiService; // Variable para almacenar la referencia al servicio RMI


    public RMIServiceAdapterImpl() throws RemoteException {
        super();
        try {
            // Obtener la referencia al servicio RMI en el constructor
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            rmiService = (RMIServiceAdapter) registry.lookup("RMIServiceAdapter");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al obtener la referencia al servicio RMI.", e);
        }
    }

    @Override
    public void createSubdirectory(String user, String parentFolderName, String subfolderName) throws RemoteException {
        try {
            // Obtén la ruta completa del directorio principal del usuario
            //String parentFolderPath = getUserFolder(user) + "\\" + parentFolderName;

            // Obtén la ruta completa del nuevo subdirectorio
            //String subfolderPath = parentFolderPath + "\\" + subfolderName;

            // Llama al método correspondiente en el servicio RMI
            rmiService.createSubdirectory(user, parentFolderName,subfolderName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al crear el subdirectorio.", e);
        }
    }


    @Override
    public void uploadFileToNode(String user, String folderName, String fileName, String fileData) throws RemoteException {
        try {
            rmiService.uploadFileToNode(user, folderName, fileName, fileData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al cargar el archivo en el nodo.", e);
        }
    }


    @Override
    public void createDirectory(String userID, String path) throws RemoteException{
        try {
            rmiService.createDirectory(userID, path);


        } catch (Exception e) {
            //e.printStackTrace();
            throw new RemoteException("Error al crear el directorio", e);
        }
    }

    @Override
    public ArrayList<String> listDirectories(String folderID) throws RemoteException{
        ArrayList<String> directoryNames = new ArrayList<>();

        try {
            directoryNames = rmiService.listDirectories(folderID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al crear el directorio", e);
        }
        return directoryNames;
    }

    @Override
    public File downloadFile(String user, String fileName) throws RemoteException {
        File file1 = null;
        try {
            file1 = rmiService.downloadFile(user, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al descargar el archivo.", e);
        }
        return file1;
    }


    @Override
    public void moveFile(String fileID, String folderID, String newFolderID)throws RemoteException{
        try {
            rmiService.moveFile(fileID, folderID, newFolderID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al crear el directorio", e);
        }
    }

    @Override
    public void renameFile(String user, String currentFileName, String newFileName) throws RemoteException {
        try {
            rmiService.renameFile(user, currentFileName, newFileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al renombrar el archivo.", e);
        }
    }


    @Override
    public ArrayList<String> listFilesInDirectory(String user, String folderName) throws RemoteException {
        ArrayList<String> fileNames = new ArrayList<>();
        try {
            fileNames = rmiService.listFilesInDirectory(user, folderName);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error inesperado al listar archivos en el directorio.");
        }
        return fileNames;
    }


    @Override
    public void deleteFile(String user, String fileName) throws RemoteException {
        try {
            rmiService.deleteFile(user, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al eliminar el archivo", e);
        }
    }



    private String getUserFolder(String user) {
        return userToFolderMap.computeIfAbsent(user, key -> {
            String folderId = generateUniqueFolderId(); // Implementa esto para generar un ID único
            try {
                createDirectory(user, folderId); // Crea la carpeta en el servidor SOAP
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            return folderId;
        });
    }

    private String generateUniqueFolderId(){
        String uniqueId = UUID.randomUUID().toString();
        return uniqueId;
    }

    private String getUserSubFolder(String user, String parentFolder) {
        String parentFolderId = getUserFolder(user);
        return parentFolderId + "/" + generateUniqueSubFolderId(); // Implementacion ID unico
    }

    private String generateUniqueSubFolderId() {
        // Generar un UUID aleatorio como ID único para la subcarpeta
        String uniqueId = UUID.randomUUID().toString();
        return uniqueId;
    }
}
