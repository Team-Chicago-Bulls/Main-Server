package com.soap.interfaces;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

import java.io.File;
import java.util.ArrayList;

@WebService
public interface ISOAPFileService {
    @WebMethod
    void createDirectory(String user, String path);

    @WebMethod
    void uploadFile(String user, String folderName, String fileName, String fileData);

    @WebMethod
    File downloadFile(String user, String fileName);

    @WebMethod
    void moveFile(String user, String sourcePath, String destinationPath);

    @WebMethod
    void renameFile(String user, String currentFileName, String newFileName);

    @WebMethod
    void deleteFile(String user, String fileName);

    @WebMethod
    void shareFile(String user, String path, String recipient);

    @WebMethod
    ArrayList<String> listFilesInDirectory(String user, String folderName);

    @WebMethod
    ArrayList<String> listDirectories(String user);
    @WebMethod
    void createSubdirectory(String user, String parentFolderName, String subfolderName);


}
