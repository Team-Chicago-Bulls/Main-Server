package interfaces;

import org.springframework.stereotype.Component;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
@Component
public interface RMIServiceAdapter extends Remote {
    void uploadFileToNode(String user, String folderName, String fileName, String base64Data) throws RemoteException;

    void createDirectory(String user, String path) throws RemoteException;

    ArrayList<String> listDirectories(String folderID) throws RemoteException;

    ArrayList<String> listFilesInDirectory(String user, String folderName) throws RemoteException;

    File downloadFile(String user, String fileName) throws RemoteException;

    void moveFile(String user, String fileName, String targetDirectory) throws RemoteException;

    void renameFile(String user, String currentFileName, String newFileName) throws RemoteException;

    void deleteFile(String user, String fileName) throws RemoteException;

    void createSubdirectory(String user, String parentFolderName, String subfolderName) throws  RemoteException;
}
