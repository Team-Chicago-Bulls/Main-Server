package com.soap.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;

import com.soap.classes.RMIServiceAdapterImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    public ResponseEntity<Map<String, Object>> createDirectory(@RequestBody Map<String, String> directorio,
            @RequestHeader Map<String, String> headers)
            throws RemoteException {
        try {
            RMIServiceAdapterImpl rmiService = new RMIServiceAdapterImpl();
            String user = getUserInfo(headers.get("authorization"));
            String path = directorio.get("path");
            rmiService.createDirectory(user, path);

            return ResponseEntity.status(200).build();
        } catch (Exception e) {

            System.out.println(e.getMessage());

        }
        return ResponseEntity.status(500).build();

    }

    @PostMapping("/createSubDirectory")
    public ResponseEntity<Map<String, Object>> createSubDirectory(@RequestBody Map<String, String> SubDirectory,
            @RequestHeader Map<String, String> headers)
            throws RemoteException {
        try {
            RMIServiceAdapterImpl rmiService = new RMIServiceAdapterImpl();
            String user = getUserInfo(headers.get("authorization"));
            String parentFolderName = SubDirectory.get("parentFolderName");
            String subfolderName = SubDirectory.get("subfolderName");
            rmiService.createSubdirectory(user, parentFolderName, subfolderName);

            return ResponseEntity.status(200).build();
        } catch (Exception e) {

            System.out.println(e.getMessage());

        }
        return ResponseEntity.status(500).build();

    }

    @PostMapping("/uploadFileToNode")
    public ResponseEntity<Map<String, Object>> uploadFileToNode(@RequestBody Map<String, String> uploadFile,
            @RequestHeader Map<String, String> headers)
            throws RemoteException {
        try {
            RMIServiceAdapterImpl rmiService = new RMIServiceAdapterImpl();

            String user = getUserInfo(headers.get("authorization"));

            // String user = uploadFile.get("user");
            String folderName = uploadFile.get("folderName");
            String fileName = uploadFile.get("fileName");
            String fileData = uploadFile.get("fileData");
            // System.out.println("user: " + user + " folderName: " + folderName + "
            // fileName: " + fileName + " fileData: " + fileData);
            Map<String, String> result = rmiService.uploadFileToNode(user, folderName, fileName, fileData);

            if (result.get("error").equals("true")) {
                return ResponseEntity.status(400).build();
            }

            result.put("id_user", user);

            DataBaseServerController.uploadFileBD(result);
            return ResponseEntity.status(200).build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(500).build();

    }

    @GetMapping("/listDirectories")
    public ResponseEntity<List<String>> listDirectories(@RequestHeader Map<String, String> headers) {
        String user = getUserInfo(headers.get("authorization"));

        try {
            RMIServiceAdapterImpl rmiService = new RMIServiceAdapterImpl();
            List<String> directories = rmiService.listDirectories(user);

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

    @GetMapping("/getFile/{id_file}")
    public ResponseEntity<InputStreamResource> getFile(@PathVariable String id_file,
            @RequestHeader Map<String, String> headers)
            throws RemoteException, FileNotFoundException {
    
        try {

            String user = getUserInfo(headers.get("authorization"));
            // String fileName = downloadFile.get("fileName");
            JsonNode file = DataBaseServerController.getFile(id_file, user);
            System.out.print(file.get(0));

            if (file != null) {

                String path = file.get(0).get("route").asText() + "/" + file.get(0).get("nombre").asText();
                String nodo = file.get(0).get("nodo").asText();

                RMIServiceAdapterImpl rmiService = new RMIServiceAdapterImpl();
                File file1 = rmiService.downloadFile(user, path, nodo);

                InputStreamResource resource = new InputStreamResource(new FileInputStream(file1));
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file1.getName())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .contentLength(file1.length())
                        .body(resource);

                // System.out.println("Archivo encontrado: " + file.get("nombre"));
                // return ResponseEntity.status(200).build();
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (RemoteException e) {

            String user = getUserInfo(headers.get("authorization"));
            JsonNode file = DataBaseServerController.getFile(id_file, user);


            String path = file.get(0).get("route_backup").asText() + "/" + file.get(0).get("nombre").asText();
            String nodo = file.get(0).get("nodo_backup").asText();

            RMIServiceAdapterImpl rmiService = new RMIServiceAdapterImpl();
            File file1 = rmiService.downloadFile(user, path, nodo);

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file1));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file1.getName())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file1.length())
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).build();
        }

    }

    @GetMapping("/downloadFile/{id_file}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String id_file,
            @RequestHeader Map<String, String> headers)
            throws RemoteException {
        File file1 = null;
        try {
            RMIServiceAdapterImpl rmiService = new RMIServiceAdapterImpl();
            String user = getUserInfo(headers.get("authorization"));
            // String fileName = downloadFile.get("fileName");
            file1 = rmiService.downloadFile(user, id_file, "a");

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file1));
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file1.getName())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(file1.length())
                    .body(resource);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.status(500).build();

    }

    // Autenticación de usuario
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
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://distribuidos4.bucaramanga.upb.edu.co/user/log_user", requestEntity, String.class);

            // Obtén el token del cuerpo de la respuesta
            String token = response.getBody();

            JsonNode tk = objectMapper.readTree(token);

            authToken = tk.get("token").asText();
            return ResponseEntity.ok(token);

        } catch (Exception e) {
            System.err.println("Error al obtener el token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    // Autenticación de usuario
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> userCredentials) {

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
            restTemplate.postForEntity("http://distribuidos4.bucaramanga.upb.edu.co/user/register_user", requestEntity,
                    String.class);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "http://distribuidos4.bucaramanga.upb.edu.co/user/log_user", requestEntity, String.class);

            // Obtén el token del cuerpo de la respuesta
            String token = response.getBody();
            JsonNode tk = objectMapper.readTree(token);

            DataBaseServerController.registerUserBd(getUserInfo(tk.get("token").asText()));

            authToken = token;
            return ResponseEntity.ok(token);

        } catch (Exception e) {
            System.err.println("Error al obtener el token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    public String getUserInfo(String authToken) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://distribuidos4.bucaramanga.upb.edu.co/user/log_user_token/" + authToken;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String user = response.getBody();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode id = objectMapper.readTree(user);

                UID = id.get("id").asText();
                return id.get("id").asText();

            } else {
                // Manejar otros códigos de estado si es necesario
                return "Error al obtener información del usuario";
            }
        } catch (Exception e) {
            // Manejar errores de comunicación con el servidor
            return "Error en la solicitud al servidor";
        }
    }

}
