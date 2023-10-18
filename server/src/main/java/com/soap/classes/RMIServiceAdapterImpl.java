package com.soap.classes;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import interfaces.RMIServiceAdapter;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.io.File;


@Component
public class RMIServiceAdapterImpl extends UnicastRemoteObject implements RMIServiceAdapter {
   
    // Estructura para mapeo de usuarios a carpetas
    private Map<String, String> userToFolderMap = new HashMap<>();
    private Map<String, String> fileIDToUserMap = new HashMap<>();
    private RMIServiceAdapter rmiService; // Variable para almacenar la referencia al servicio RMI
    Map<String,String> nodos = new HashMap<String,String>();

    Registry registry;

    public RMIServiceAdapterImpl() throws RemoteException {
        super();
        try {
            // Obtener la referencia al servicio RMI en el constructor
            //Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            //rmiService = (RMIServiceAdapter) registry.lookup("RMIServiceAdapter");
        
            nodos.put("nodo1", "10.152.164.230");
            nodos.put("nodo2", "10.152.164.231");
            nodos.put("nodo3", "10.152.164.232");
            //nodos.put("nodo4", "10.152.164.233");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al obtener la referencia al servicio RMI.", e);
        }
    }

    public String[] getNodos() {
        String[] nodos = new String[2];
        Random random = new Random();
        String nodoPrincipal = "nodo" + String.valueOf(1 + random.nextInt(4));
        String nodoCopia = "nodo" + String.valueOf(1 + random.nextInt(4));

        while (nodoPrincipal.equals(nodoCopia)) {
            nodoCopia = "nodo" + String.valueOf(1 + random.nextInt(4));
        }

        nodos[0] = nodoPrincipal;
        nodos[1] = nodoCopia;


        return nodos;
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
    public Map<String,String> uploadFileToNode(String user, String folderName, String fileName, String fileData) throws RemoteException {
        try {
            String[] nodos_numero =  getNodos();



            registry = LocateRegistry.getRegistry(nodos.get(nodos_numero[0]), 1099);
            rmiService = (RMIServiceAdapter) registry.lookup("RMIServiceAdapter");

            Map<String,String> principal = rmiService.uploadFileToNode(user, folderName, fileName, fileData);

            registry = LocateRegistry.getRegistry(nodos.get(nodos_numero[1]), 1099);
            rmiService = (RMIServiceAdapter) registry.lookup("RMIServiceAdapter");

            Map<String,String> copia = rmiService.uploadFileToNode(user, folderName, fileName, fileData);

            Map<String,String> resultado = new HashMap<String,String>();
            
            resultado.put("error", principal.get("error"));
            resultado.put("nombre", fileName);
            resultado.put("nodo", nodos_numero[0]);
            resultado.put("size", principal.get("size"));
            resultado.put("ruta", principal.get("ruta"));
            resultado.put("nodo_copia", nodos_numero[1]);
            resultado.put("route_copia", copia.get("ruta"));
            

            return resultado;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al cargar el archivo en el nodo.", e);
        }
        
    }


    @Override
    public void createDirectory(String userID, String path) throws RemoteException{
        try {

            Set<String> keys = nodos.keySet();

            for(String key: keys){
                registry = LocateRegistry.getRegistry(nodos.get(key), 1099);
                rmiService = (RMIServiceAdapter) registry.lookup("RMIServiceAdapter");
                rmiService.createDirectory(userID, path);
            }

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
