package com.soap.classes;

import com.soap.interfaces.RMIServiceAdapter;
import com.soap.classes.File; // Importa la clase File si es necesaria

import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServiceAdapterImpl extends UnicastRemoteObject implements RMIServiceAdapter {
    public RMIServiceAdapterImpl() throws RemoteException {
        super();
    }

    @Override
    public void uploadFileToNode(String filePath, byte[] fileData) throws RemoteException {
        try {
            // Obtener la ruta del nodo y el nombre del archivo
            String[] parts = filePath.split("/");
            String nodeName = parts[parts.length - 2]; // Obtiene el nombre del nodo desde la ruta

            // Especifica la ubicación en la que deseas guardar el archivo en el nodo
            String nodeDirectory = "/ruta/en/el/nodo/" + nodeName; // Reemplaza con la ruta adecuada en el nodo

            // Crea una ruta completa para guardar el archivo en el nodo
            String nodeFilePath = nodeDirectory + "/" + parts[parts.length - 1]; // Obtiene el nombre del archivo desde la ruta

            // Guarda el archivo en el sistema de archivos del nodo
            try (FileOutputStream fos = new FileOutputStream(nodeFilePath)) {
                fos.write(fileData);
                System.out.println("Archivo '" + parts[parts.length - 1] + "' guardado en el nodo '" + nodeName + "'.");
            } catch (Exception e) {
                e.printStackTrace();
                throw new RemoteException("Error al guardar el archivo en el nodo.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error al cargar el archivo en el nodo.");
        }
    }
}
