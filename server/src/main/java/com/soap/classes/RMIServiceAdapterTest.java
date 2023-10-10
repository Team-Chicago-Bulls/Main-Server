package com.soap.classes;

import interfaces.RMIServiceAdapter;


public class RMIServiceAdapterTest {
    public static void main(String[] args) {
        try {
            RMIServiceAdapter rmiService = new RMIServiceAdapterImpl();

            rmiService.listDirectories("usuario");

            // Realiza más pruebas llamando a otros métodos de NodeService
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

