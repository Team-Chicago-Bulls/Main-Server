package com.soap.interfaces;

import java.util.ArrayList;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface ISOAPFileService {
    @WebMethod
    void createDirectory(String path);

    @WebMethod
    void uploadFile(String directory, byte[] fileData);

    @WebMethod
    byte[] downloadFile(String path);

    @WebMethod
    void moveOrRenameFile(String oldPath, String newPath);

    @WebMethod
    void deleteFile(String path);

    @WebMethod
    void shareFile(String path, String user);

    @WebMethod
    ArrayList<String> listFilesInDirectory(String path);
}
