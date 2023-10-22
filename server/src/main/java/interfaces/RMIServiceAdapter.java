package interfaces;

import org.springframework.stereotype.Component;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

@Component
public interface RMIServiceAdapter extends Remote {
    Map<String, String> uploadFileToNode(String user, String folderName, String fileName, String base64Data)
            throws RemoteException;

    boolean createDirectory(String user, String path) throws RemoteException;

    ArrayList<String> listDirectories(String folderID) throws RemoteException;

    ArrayList<String> listFilesInDirectory(String user, String folderName) throws RemoteException;

    byte[] downloadFile(String user, String fileName) throws RemoteException;

    String moveFile(String user, String fileName, String targetDirectory) throws RemoteException;

    boolean renameFile(String user, String currentFileName, String newFileName) throws RemoteException;

    boolean deleteFile(String user, String fileName) throws RemoteException;

    boolean createSubdirectory(String user, String parentFolderName, String subfolderName) throws RemoteException;

    void shareFile(String sourceUser, String destinationUser, String fileName) throws RemoteException;
}
