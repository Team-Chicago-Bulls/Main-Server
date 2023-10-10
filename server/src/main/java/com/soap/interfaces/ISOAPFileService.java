package com.soap.interfaces;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

import java.util.ArrayList;

@WebService
public interface ISOAPFileService {
    @WebMethod
    void createDirectory(String user, String path);

    @WebMethod
    void uploadFile(String folderID, String fileName, byte[] fileData);

    @WebMethod
    byte[] downloadFile(String fileID);

    @WebMethod
    void moveFile(String user, String sourcePath, String destinationPath);

    @WebMethod
    void renameFile(String fileID, String newName);

    @WebMethod
    void deleteFile(String fileID);

    @WebMethod
    void shareFile(String user, String path, String recipient);

    @WebMethod
    ArrayList<String> listFilesInDirectory(String folderID);

    @WebMethod
    ArrayList<String> listDirectories(String user);


}
