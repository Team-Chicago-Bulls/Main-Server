package com.soap.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import interfaces.RMIServiceAdapter;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;

import com.soap.classes.RMIServiceAdapterImpl;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DataBaseServerController {

    public DataBaseServerController() {

    }

    public void uploadFileBD(Map<String, String> resultado) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "http://distribuidos2.bucaramanga.upb.edu.co/api/file";

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(resultado);

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            restTemplate.postForEntity(url, requestEntity, String.class);

        } catch (Exception e) {
            System.err.println("Error al obtener el token: " + e.getMessage());
        }
    }

    public void moveFileBD(String file_id, String user_id, String ruta) {
        try {

            String url = "http://distribuidos2.bucaramanga.upb.edu.co/api/file/route";

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> body = new HashMap<>();
            body.put("file_id", file_id);
            body.put("user_id", user_id);
            body.put("route", ruta);

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(body);
          
            java.net.http.HttpRequest request =  java.net.http.HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .method("PATCH", java.net.http.HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            java.net.http.HttpResponse<String> response = java.net.http.HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());

            System.out.println(response.statusCode());
            System.out.println(response.body());

        } catch (Exception e) {
            System.err.println("Error al obtener el token: " + e.getMessage());
        }
    }

    public void renameFileBD(String nombre, String file_id, String user_id) {
        try {
       
            String url = "http://distribuidos2.bucaramanga.upb.edu.co/api/file/name";

            Map<String, String> body = new HashMap<>();
            body.put("name", nombre);
            body.put("user_id", user_id);
            body.put("file_id", file_id);

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(body);
          
            java.net.http.HttpRequest request =  java.net.http.HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .method("PATCH", java.net.http.HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            java.net.http.HttpResponse<String> response = java.net.http.HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());

            System.out.println(response.statusCode());
            System.out.println(response.body());

           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Map<String,Object>> getFilesList(String id_user) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "http://distribuidos2.bucaramanga.upb.edu.co//api/file/list?id=" + id_user;

            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<JsonNode> response = restTemplate.getForEntity(url,
                    JsonNode.class, headers);

            JsonNode body = response.getBody();

            if (response == null || body.get("error").asBoolean() == true) {
                return null;
            }

            Map<String, Object> resultado = new HashMap<String, Object>();

            JsonNode list = body.get("msg");

            List<Map<String, Object>> lista = new ArrayList<Map<String, Object>>();

            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> file = new HashMap<String, Object>();
                file.put("id", list.get(i).get("id").asText());
                file.put("name", list.get(i).get("name").asText());
                file.put("size", list.get(i).get("size").asText());
                file.put("nodo", list.get(i).get("nodo").asText());
                file.put("route", list.get(i).get("route").asText());
                file.put("nodo_backup", list.get(i).get("nodo_backup").asText());
                file.put("route_backup", list.get(i).get("route_backup").asText());
                lista.add(file);
            }

            //resultado.put("files", lista);

            return lista;

        } catch (Exception e) {
            System.err.println("Error al obtener el token: " + e.getMessage());
            return null;
        }
    }


    public Map<String, Object> getFile(String id_file, String id_user) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "http://distribuidos2.bucaramanga.upb.edu.co/api/file?user_id=" + id_user + "&file_id="
                    + id_file;

            System.out.println(url);
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<JsonNode> response = restTemplate.getForEntity(url,
                    JsonNode.class, headers);

            JsonNode body = response.getBody();
            System.out.println(body);
            if (response == null || body.get("error").asBoolean() == true) {
                return null;
            }

            Map<String, Object> resultado = new HashMap<String, Object>();

            JsonNode list = body.get("msg");

            resultado.put("id", list.get(0).get("id").asText());
            resultado.put("name", list.get(0).get("name").asText());
            resultado.put("size", list.get(0).get("size").asText());
            resultado.put("nodo", list.get(0).get("nodo").asText());
            resultado.put("route", list.get(0).get("route").asText());
            resultado.put("nodo_backup", list.get(0).get("nodo_backup").asText());
            resultado.put("route_backup", list.get(0).get("route_backup").asText());

            return resultado;

        } catch (Exception e) {
            System.err.println("Error al obtener el token: " + e.getMessage());
            return null;
        }
    }

    public void registerUserBd(String id) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = "http://distribuidos2.bucaramanga.upb.edu.co/api/user";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> idMap = new HashMap<>();
            idMap.put("id", id);

            ObjectMapper objectMapper = new ObjectMapper();
            String requestBody = objectMapper.writeValueAsString(idMap);

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            restTemplate.postForEntity(url, requestEntity, String.class);

        } catch (Exception e) {
            System.err.println("Error al obtener el token: " + e.getMessage());
        }
    }

}
