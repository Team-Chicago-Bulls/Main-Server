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
public class RMIServiceAdapterImpl {
   
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
        
            nodos.put("nodo1", "10.154.12.112");
            nodos.put("nodo2", "10.154.12.116");
            nodos.put("nodo3", "10.154.12.115");
            nodos.put("nodo4", "10.154.12.117");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al obtener la referencia al servicio RMI.", e);
        }
    }

    public String[] getNodos() {
        String[] nodos = new String[2];
        Random random = new Random();
        String nodoPrincipal = "nodo" + String.valueOf(1 + random.nextInt(this.nodos.size()));
        String nodoCopia = "nodo" + String.valueOf(1 + random.nextInt(this.nodos.size()));

        while (nodoPrincipal.equals(nodoCopia)) {
            nodoCopia = "nodo" + String.valueOf(1 + random.nextInt(this.nodos.size()));
        }

        nodos[0] = nodoPrincipal;
        nodos[1] = nodoCopia;


        return nodos;
    }

    
    public boolean createSubdirectory(String user, String parentFolderName, String subfolderName) throws RemoteException {
        try {
            // Obtén la ruta completa del directorio principal del usuario
            //String parentFolderPath = getUserFolder(user) + "\\" + parentFolderName;

            // Obtén la ruta completa del nuevo subdirectorio
            //String subfolderPath = parentFolderPath + "\\" + subfolderName;

            // Llama al método correspondiente en el servicio RMI
           
           
            rmiService.createSubdirectory(user, parentFolderName,subfolderName);
            return true;


        } catch (Exception e) {
            e.printStackTrace();
            return false;
            //throw new RemoteException("Error al crear el subdirectorio.", e);
        }
    }


    
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


    
    public boolean createDirectory(String userID, String path) throws RemoteException{
        try {

            Set<String> keys = nodos.keySet();

            for(String key: keys){
                registry = LocateRegistry.getRegistry(nodos.get(key), 1099);
                rmiService = (RMIServiceAdapter) registry.lookup("RMIServiceAdapter");
                rmiService.createDirectory(userID, path);
            }
            return true;

        } catch (Exception e) {
            //e.printStackTrace();
            return false;
            //throw new RemoteException("Error al crear el directorio", e);
        }
    }

    
    public ArrayList<String> listDirectories(String folderID) throws RemoteException{
        ArrayList<String> directoryNames = new ArrayList<>();

        try {
            registry = LocateRegistry.getRegistry(nodos.get("nodo1"), 1099);
            rmiService = (RMIServiceAdapter) registry.lookup("RMIServiceAdapter");
            directoryNames = rmiService.listDirectories(folderID);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al crear el directorio", e);
        }
        return directoryNames;
    }
    
    public byte[] downloadFile(String user, String fileName, String node) throws RemoteException {
        byte[] file1 = null;
        try {
            registry = LocateRegistry.getRegistry(nodos.get(node), 1099);
            rmiService = (RMIServiceAdapter) registry.lookup("RMIServiceAdapter");
            file1 = rmiService.downloadFile(user, fileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al descargar el archivo.", e);
        }
        return file1;
    }




    
    public String moveFile(String nodo, String fileID, String folderID, String newFolderID)throws RemoteException{
        try {
            registry = LocateRegistry.getRegistry(nodos.get(nodo), 1099);
            rmiService = (RMIServiceAdapter) registry.lookup("RMIServiceAdapter");
            rmiService.moveFile(fileID, folderID, newFolderID);
            return rmiService.moveFile(fileID, folderID, newFolderID);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
            //throw new RemoteException("Error al crear el directorio", e);
        }
    }

    
    public boolean renameFile(String nodo, String path, String currentFileName, String newFileName) throws RemoteException {
        try {
            registry = LocateRegistry.getRegistry(nodos.get(nodo), 1099);
            rmiService = (RMIServiceAdapter) registry.lookup("RMIServiceAdapter");
            rmiService.renameFile(path, currentFileName, newFileName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
            //throw new RemoteException("Error al renombrar el archivo.", e);
        }
    }


    
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


    
    public boolean deleteFile(String user, String fileName) throws RemoteException {
        try {
            rmiService.deleteFile(user, fileName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
            //throw new RemoteException("Error al eliminar el archivo", e);
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
