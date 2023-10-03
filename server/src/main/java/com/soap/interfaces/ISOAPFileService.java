package com.soap.interfaces;

import java.util.ArrayList;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface ISOAPFileService {
    @WebMethod
    void createDirectory(String path);

    @WebMethod
    void uploadFile(String directory, String fileName, byte[] fileData);

    @WebMethod
    byte[] downloadFile(String path);

    @WebMethod
    void moveFile(String sourcePath, String destinationPath);

    @WebMethod
    void renameFile(String oldPath, String newName);

    @WebMethod
    void deleteFile(String path);

    @WebMethod
    void shareFile(String path, String user);

    @WebMethod
    ArrayList<String> listFilesInDirectory(String path);

    @WebMethod
    ArrayList<String> listDirectories();
}
