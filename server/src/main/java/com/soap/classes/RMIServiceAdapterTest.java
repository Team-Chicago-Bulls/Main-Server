package com.soap.classes;



import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import interfaces.RMIServiceAdapter;

public class RMIServiceAdapterTest {
    public static void main(String[] args) {
        try {
            RMIServiceAdapter rmiService = new RMIServiceAdapterImpl();
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("Menú:");
                System.out.println("1. Crear directorio");
                System.out.println("2. Listar directorios");
                System.out.println("3. Listar archivos en directorio");
                System.out.println("4. Subir archivo");
                System.out.println("5. Descargar archivo");
                System.out.println("6. Renombrar archivo");
                System.out.println("7. Mover archivo");
                System.out.println("8. Eliminar archivo");
                System.out.println("9. Crear subdirectorio");
                System.out.println("10. Salir");
                System.out.print("Seleccione una opción: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.print("Ingrese el nombre del directorio a crear: ");
                        String dirName = scanner.next();
                        rmiService.createDirectory("jossyr", dirName);
                        System.out.println("Directorio creado con éxito.");
                        break;
                    case 2:
                        ArrayList<String> directories = rmiService.listDirectories("jossyr");
                        System.out.println("Directorios disponibles:");
                        for (String dir : directories) {
                            System.out.println(dir);
                        }
                        break;
                    case 3:
                        System.out.print("Ingrese el nombre del directorio para listar archivos: ");
                        String dirToList = scanner.next();
                        ArrayList<String> filesInDir = rmiService.listFilesInDirectory("jossyr", dirToList);
                        System.out.println("Archivos en el directorio:");
                        for (String fileName : filesInDir) {
                            System.out.println(fileName);
                        }
                        break;
                    case 4:
                        System.out.print("Ingrese el nombre del directorio donde desea subir el archivo: ");
                        String uploadDir = scanner.next();
                        System.out.print("Ingrese el nombre del archivo: ");
                        String fileName = scanner.next();
                        System.out.print("Ingrese el contenido del archivo: ");
                        String fileContent = scanner.next();
                        rmiService.uploadFileToNode("jossyr", uploadDir, fileName, fileContent);
                        System.out.println("Archivo subido con éxito.");
                        break;
                    case 5:
                        System.out.print("Ingrese el nombre del archivo para descargar: ");
                        String fileToDownload = scanner.next();
                        File fileData = rmiService.downloadFile("jossyr", fileToDownload);
                        if (fileData != null) {
                            System.out.println("Contenido del archivo:");
                            System.out.println("Archivo descargado");
                        } else {
                            System.out.println("El archivo no pudo ser descargado.");
                        }
                        break;
                    case 6:
                        System.out.print("Ingrese el nombre del archivo a renombrar: ");
                        String oldFileName = scanner.next();
                        System.out.print("Ingrese el nuevo nombre: ");
                        String newFileName = scanner.next();
                        rmiService.renameFile("jossyr", oldFileName, newFileName);
                        System.out.println("Archivo renombrado con éxito.");
                        break;
                    case 7:
                        System.out.print("Ingrese el nombre del archivo: ");
                        String fileN = scanner.next();
                        System.out.print("Ingrese la nueva ubicación del archivo (ruta completa sin el nombre de archivo: ");
                        String destinationPath = scanner.next();
                        rmiService.moveFile("jossyr",fileN,destinationPath);
                        break;

                    case 8:
                        System.out.print("Ingrese el nombre del archivo a eliminar: ");
                        String fileToDelete = scanner.next();
                        rmiService.deleteFile("jossyr", fileToDelete);
                        System.out.println("Archivo eliminado con éxito.");
                        break;
                    case 9:
                        System.out.print("Ingrese el nombre del directorio principal: ");
                        String parentDir = scanner.next();
                        System.out.print("Ingrese el nombre del subdirectorio a crear: ");
                        String subDir = scanner.next();
                        rmiService.createSubdirectory("jossyr", parentDir, subDir);
                        System.out.println("Subdirectorio creado con éxito.");
                        break;
                    case 10:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtelo de nuevo.");
                        break;
                }
            } while (choice != 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
