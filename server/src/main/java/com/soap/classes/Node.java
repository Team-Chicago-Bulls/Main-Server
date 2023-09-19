package com.soap.classes;

import com.soap.interfaces.NodeService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Node implements NodeService {
    public Node() throws RemoteException {
        super();
    }

    @Override
    public void uploadFile(File file) throws RemoteException {
        // Implementa la lógica para guardar el archivo en el nodo.
        // Puedes utilizar la información del archivo para guardarlo en el nodo adecuado.
        // Por ejemplo, puedes utilizar el nombre del archivo o alguna lógica de distribución.
    }
}
