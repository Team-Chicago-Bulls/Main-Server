package com.soap.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServiceAdapter extends Remote {
    void uploadFileToNode(String filePath, byte[] fileData) throws RemoteException;
}
