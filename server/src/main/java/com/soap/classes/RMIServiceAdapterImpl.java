package com.soap.classes;

import interfaces.RMIServiceAdapter;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RMIServiceAdapterImpl extends UnicastRemoteObject implements RMIServiceAdapter {
    private static final String BASE_DIRECTORY = "C:\\Users\\USER\\Documents\\Universidad\\LastSemester\\Distribuidos\\Proyecto de Aula\\ArchivosSOAP";
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
    public void uploadFileToNode(String folderID, String fileName, byte[] fileData) throws RemoteException {
        try {
            // Llama al método correspondiente en el servicio RMI
            rmiService.uploadFileToNode(folderID, fileName, fileData); // Usar el mismo nombre del método y parámetros
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al cargar el archivo en el nodo.");
        }
    }

    @Override
    public void createDirectory(String userID, String path) throws RemoteException{
        try {
            System.out.println("hola");
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
            rmiService.listDirectories(folderID);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al crear el directorio", e);
        }

        return directoryNames;
    }

    @Override
    public byte[] downloadFile(String fileID) throws RemoteException{
        byte[] file;
        try {
            file = rmiService.downloadFile(fileID);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al crear el directorio", e);
        }
        return file;
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
    public void renameFile(String fileID, String newName)throws RemoteException{
        try {
            rmiService.renameFile(fileID, newName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al crear el directorio", e);
        }
    }

    @Override
    public ArrayList<String> listFilesInDirectory(String folderID) throws RemoteException{
        ArrayList<String> fileNames = new ArrayList<>();
        try {
            fileNames = rmiService.listFilesInDirectory(folderID);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error inesperado al renombrar el archivo.");
        }
        return fileNames;
    }

    @Override
    public void deleteFile(String fileID)throws RemoteException{
        try {
            rmiService.deleteFile(fileID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al crear el directorio", e);
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
