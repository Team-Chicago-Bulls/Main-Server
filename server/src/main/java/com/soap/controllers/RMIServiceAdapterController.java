package com.soap.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import interfaces.RMIServiceAdapter;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;




import com.soap.classes.RMIServiceAdapterImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class RMIServiceAdapterController {
    public String authToken;
    public String UID;

    @PostMapping("/createDirectory")
    public ResponseEntity<Map<String, Object>> createDirectory(@RequestBody Map<String, String>directorio) throws RemoteException {
        try {
            RMIServiceAdapter rmiService = new RMIServiceAdapterImpl();
            String user = directorio.get("user");
            String path = directorio.get("path");
            rmiService.createDirectory(user, path);

            return ResponseEntity.status(200).build();
        }catch (Exception e){

            System.out.println(e.getMessage());

        }
        return ResponseEntity.status(500).build();

    }
    @PostMapping("/createSubDirectory")
    public ResponseEntity<Map<String, Object>> createSubDirectory(@RequestBody Map<String, String>SubDirectory) throws RemoteException {
        try {
            RMIServiceAdapter rmiService = new RMIServiceAdapterImpl();
            String user = SubDirectory.get("user");
            String parentFolderName = SubDirectory.get("parentFolderName");
            String subfolderName = SubDirectory.get("subfolderName");
            rmiService.createSubdirectory(user, parentFolderName, subfolderName);

            return ResponseEntity.status(200).build();
        }catch (Exception e){

            System.out.println(e.getMessage());

        }
        return ResponseEntity.status(500).build();

    }
    @PostMapping("/uploadFileToNode")
    public ResponseEntity<Map<String, Object>> uploadFileToNode(@RequestBody Map<String, String>uploadFile) throws RemoteException{
        try{
            RMIServiceAdapter rmiService = new RMIServiceAdapterImpl();
            String user = uploadFile.get("user");
            String folderName = uploadFile.get("folderName");
            String fileName = uploadFile.get("fileName");
            String fileData = uploadFile.get("fileData");
            rmiService.uploadFileToNode(user, folderName, fileName, fileData);
            return ResponseEntity.status(200).build();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(500).build();

    }
    @GetMapping("/listDirectories")
    public ResponseEntity<List<String>> listDirectories(@RequestBody Map<String, String> listDirectories) {
        String folderID = listDirectories.get("folderID");

        try {
            RMIServiceAdapter rmiService = new RMIServiceAdapterImpl();
            List<String> directories = rmiService.listDirectories(folderID);

            if (directories != null) {
                System.out.println("Directorios encontrados: " + directories.size());
                return new ResponseEntity<>(directories, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (RemoteException e) {
            System.err.println("Error al listar directorios: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/downloadFile")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestBody Map<String, String>downloadFile) throws RemoteException{
        File file1 = null;
        try{
            RMIServiceAdapter rmiService = new RMIServiceAdapterImpl();
            String user = downloadFile.get("user");
            String fileName = downloadFile.get("fileName");
            file1 = rmiService.downloadFile(user, fileName);
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file1));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file1.getName())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file1.length())
                    .body(resource);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(500).build();

    }

    //Autenticación de usuario
    @PostMapping("/getAuthToken")
    public ResponseEntity<String> getAuthToken(@RequestBody Map<String, String> userCredentials) {

        try {

            // Configura RestTemplate
            RestTemplate restTemplate = new RestTemplate();

            // Crea un encabezado con el tipo de contenido
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Obtén el nombre de usuario y contraseña del cuerpo de la solicitud
            String correo = userCredentials.get("correo");
            String contrasena = userCredentials.get("contrasena");


            // Construye un objeto JSON con las credenciales
            Map<String, String> credentialsMap = new HashMap<>();
            credentialsMap.put("correo", correo);
            credentialsMap.put("contrasena", contrasena);

            // Convierte el objeto JSON a una cadena
            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(credentialsMap);


            // Crea una entidad HTTP con el encabezado y el cuerpo
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            // Realiza una solicitud POST al servidor de autenticación
            ResponseEntity<String> response = restTemplate.postForEntity("http://distribuidos4.bucaramanga.upb.edu.co/user/log_user", requestEntity, String.class);

            // Obtén el token del cuerpo de la respuesta
            String token = response.getBody();
            System.out.println(token);
            authToken = token;
            return ResponseEntity.ok(token);

        } catch (Exception e) {
            System.err.println("Error al obtener el token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


    }

    @GetMapping("/getUserId")
    public ResponseEntity<String> getUserInfo(@RequestParam("token") String authToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://distribuidos4.bucaramanga.upb.edu.co/user/log_user_token/" + authToken;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String user = response.getBody();
                System.out.println(user);
                UID = user;
                return ResponseEntity.ok(user);

            } else {
                // Manejar otros códigos de estado si es necesario
                return ResponseEntity.status(response.getStatusCode()).body("Error al obtener información del usuario");
            }
        } catch (RestClientException e) {
            // Manejar errores de comunicación con el servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error en la solicitud al servidor");
        }
    }



}
