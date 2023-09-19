package com.soap.classes;

import com.soap.interfaces.NodeService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NodeMain {
    public static void main(String[] args) {
        try {
            // Crear una instancia del nodo
            Node node = new Node();

            // Exportar el objeto remoto
            NodeService nodeService = (NodeService) node;

            // Registrar el nodo en el registro RMI
            Registry registry = LocateRegistry.createRegistry(1099); // Usa el puerto 1099 o el que desees
            registry.rebind("NodeService", nodeService);

            System.out.println("Nodo RMI iniciado y registrado.");
        } catch (Exception e) {
            System.err.println("Error al iniciar el nodo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
