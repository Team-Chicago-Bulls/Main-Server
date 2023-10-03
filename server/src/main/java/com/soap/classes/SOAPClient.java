package com.soap.classes;

import com.soap.interfaces.ISOAPFileService;
import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebServiceException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.namespace.QName;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class SOAPClient {
    public static void main(String[] args) {
        try {
            // URL del servicio web SOAP
            URL url = new URL("http://localhost:8080/SOAP?wsdl");
            Service service = Service.create(url, new QName("http://classes.soap.com/", "SOAPFileServiceService"));

            // Obtener el puerto del servicio
            ISOAPFileService port = service.getPort(ISOAPFileService.class);

            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("Menú:");
                System.out.println("1. Crear directorio");
                System.out.println("2. Listar archivos en directorio");
                System.out.println("3. Listar directorios disponibles");
                System.out.println("4. Subir archivo");
                System.out.println("5. Leer y descargar archivo");
                System.out.println("6. Renombrar archivo");
                System.out.println("7. Mover archivo");
                System.out.println("8. Eliminar archivo");
                System.out.println("9. Salir");
                System.out.print("Seleccione una opción: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        // Crear directorio en el servidor
                        System.out.print("Ingrese el nombre del directorio a crear: ");
                        String newDir = scanner.next();
                        port.createDirectory(newDir);
                        System.out.println("Directorio creado con éxito.");
                        break;
                    case 2:
                        // Listar archivos en un directorio
                        System.out.print("Ingrese la ruta del directorio a listar: ");
                        String dirToList = scanner.next();
                        ArrayList<String> filesInDir = port.listFilesInDirectory(dirToList);
                        for (String fileName : filesInDir) {
                            System.out.println("Nombre de archivo: " + fileName);
                        }
                        break;
                    case 3:
                        // Listar directorios disponibles en el servidor
                        ArrayList<String> directories = port.listDirectories();
                        System.out.println("Directorios disponibles:");
                        for (String dir : directories) {
                            System.out.println(dir);
                        }
                        break;
                    case 4:
                        // Subir archivo al servidor
                        System.out.print("Ingrese el directorio donde desea subir el archivo: ");
                        String directory = scanner.next();

                        // Solicitar al usuario la ruta local del archivo
                        System.out.print("Ingrese la ruta del archivo en su sistema local: ");
                        String localFilePath = scanner.next();

                        // Leer el contenido del archivo local y cargarlo en el servidor
                        try {
                            Path localFile = Paths.get(localFilePath);
                            byte[] fileData = Files.readAllBytes(localFile);

                            // Llamar al método uploadFile con los argumentos correctos
                            port.uploadFile(directory, localFile.getFileName().toString(), fileData);
                            System.out.println("Archivo subido con éxito.");
                        } catch (IOException e) {
                            System.err.println("Error al leer el archivo local.");
                            e.printStackTrace();
                        }
                        break;
                    case 5:
                        // Leer y descargar archivo del servidor
                        System.out.print("Ingrese la ruta del archivo que desea leer/descargar: ");
                        String fileToRead = scanner.next();

                        // Llamar al método downloadFile del servicio SOAP
                        byte[] fileContent = port.downloadFile(fileToRead);
                        if (fileContent != null) {
                            String fileName = Paths.get(fileToRead).getFileName().toString();
                            String extension = getFileExtension(fileName);

                            if (extension != null && (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif"))) {
                                // Si la extensión es de una imagen (por ejemplo, jpg, png, gif), muestra la imagen
                                displayImage(fileContent);
                            } else {
                                // Si no es una imagen, muestra el contenido como texto
                                System.out.println("Contenido del archivo '" + fileName + "':");
                                System.out.println(new String(fileContent));
                            }
                        } else {
                            // Si no se pudo leer, ofrecer la opción de descargarlo
                            System.out.println("No se pudo leer el archivo.");
                            System.out.print("¿Desea descargarlo en su sistema local? (S/N): ");
                            String downloadChoice = scanner.next();
                            if (downloadChoice.equalsIgnoreCase("S")) {
                                // Guardar el archivo en el sistema local
                                System.out.print("Ingrese la ruta donde desea guardar el archivo localmente: ");
                                String localSavePath = scanner.next();
                                try {
                                    FileOutputStream fos = new FileOutputStream(localSavePath);
                                    fos.write(fileContent);
                                    fos.close();
                                    System.out.println("Archivo descargado y guardado con éxito.");
                                } catch (IOException e) {
                                    System.err.println("Error al guardar el archivo localmente.");
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;

                    case 6:
                        // Renombrar archivo en el servidor
                        System.out.print("Ingrese la ruta del archivo que desea renombrar: ");
                        String oldPath = scanner.next();
                        System.out.print("Ingrese el nuevo nombre del archivo: ");
                        String newName = scanner.next();
                        port.renameFile(oldPath, newName);
                        System.out.println("Archivo renombrado con éxito.");
                        break;
                    case 7:
                        // Mover archivo en el servidor
                        System.out.print("Ingrese la ruta del archivo que desea mover (incluyendo el nombre del archivo, ej: /documentos/archivo.txt): ");
                        String sourcePath = scanner.next();
                        System.out.print("Ingrese la nueva ubicación del archivo (ruta completa sin el nombre de archivo, ej: /archivos): ");
                        String destinationPath = scanner.next();
                        port.moveFile(sourcePath, destinationPath);
                        System.out.println("Archivo movido con éxito.");
                        break;


                    case 8:
                        // Eliminar archivo en el servidor
                        System.out.print("Ingrese la ruta del archivo que desea eliminar: ");
                        String fileToDelete = scanner.next();
                        port.deleteFile(fileToDelete);
                        System.out.println("Archivo eliminado con éxito.");
                        break;

                    case 9:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opción no válida. Inténtelo de nuevo.");
                        break;

                }
            } while (choice != 9);
        } catch (WebServiceException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex + 1);
        }
        return null;
    }

    private static void displayImage(byte[] imageData) {
        try {
            // Lee la imagen desde los datos de bytes
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageData));

            // Crea una ventana para mostrar la imagen
            JFrame frame = new JFrame("Imagen");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Agrega un componente para mostrar la imagen
            JLabel label = new JLabel(new ImageIcon(img));
            frame.getContentPane().add(label, BorderLayout.CENTER);

            // Ajusta automáticamente el tamaño de la ventana según el tamaño de la imagen
            frame.pack();
            frame.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al mostrar la imagen.");
        }
    }
}
