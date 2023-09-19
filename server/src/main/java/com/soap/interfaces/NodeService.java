package com.soap.interfaces;

import com.soap.classes.File;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NodeService extends Remote {
    void uploadFile(File file) throws RemoteException;
}
