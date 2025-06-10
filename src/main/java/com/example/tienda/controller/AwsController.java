package com.example.tienda.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.tienda.model.Asset;
import com.example.tienda.service.AwsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
public class AwsController {

    private final AwsService awsService;

    // Obtener contenido de un archivo en texto plano
    @GetMapping("/files/content")
    public ResponseEntity<String> getFileContent(@RequestParam String fileName) {
        try {
            String content = awsService.getS3FileContent(fileName);
            return ResponseEntity.ok(content);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al leer contenido del archivo.");
        }
    }

    // Listar archivos en el bucket
    @GetMapping("/files")
    public ResponseEntity<List<Asset>> listFiles() {
        try {
            List<Asset> files = awsService.getS3Files();
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Descargar archivo
    @GetMapping("/files/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam String fileName) {
        try {
            byte[] data = awsService.downloadFile(fileName);
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                    .contentLength(data.length)
                    .header("Content-Type", "application/octet-stream")
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Eliminar archivo
    @DeleteMapping("/files")
    public ResponseEntity<String> deleteFile(@RequestParam String fileName) {
        try {
            awsService.deleteObject(fileName);
            return ResponseEntity.ok("Archivo eliminado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar archivo.");
        }
    }

    // Mover archivo
    @PutMapping("/files/move")
    public ResponseEntity<String> moveFile(@RequestParam String sourceKey,
                                           @RequestParam String destinationKey) {
        try {
            awsService.moveObject(sourceKey, destinationKey);
            return ResponseEntity.ok("Archivo movido correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al mover archivo.");
        }
    }

    // Subir archivo a un path personalizado (por ejemplo: clienteId/2024-06)
    @PostMapping("/files")
    public ResponseEntity<String> uploadFile(@RequestParam String path,
                                             @RequestParam MultipartFile file) {
        try {
            String fileName = awsService.uploadFile(path, file);
            return ResponseEntity.ok("Archivo subido a: " + path + "/" + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir archivo.");
        }
    }
}
