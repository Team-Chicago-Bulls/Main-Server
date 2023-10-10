package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RMIServiceAdapter extends Remote {
    void uploadFileToNode(String folderID, String fileName, byte[] fileData) throws RemoteException;

    void createDirectory(String user, String path) throws RemoteException;

    ArrayList<String> listDirectories(String folderID) throws RemoteException;

    ArrayList<String> listFilesInDirectory(String folderID) throws RemoteException;

    byte[] downloadFile(String fileID) throws RemoteException;

    void moveFile(String fileID, String folderID, String newFolderID) throws RemoteException;

    void renameFile(String fileID, String newName) throws RemoteException;

    void deleteFile(String fileID) throws RemoteException;
}
