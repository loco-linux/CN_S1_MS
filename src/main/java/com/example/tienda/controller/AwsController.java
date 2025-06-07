package com.example.tienda.controller;

import com.example.tienda.model.Asset;
import com.example.tienda.service.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/s3")
public class AwsController {

    private final AwsService awsService;

    @Autowired
    public AwsController(AwsService awsService) {
        this.awsService = awsService;
    }

    @GetMapping("/getS3FileContent")
    public ResponseEntity<String> getS3FileContent(@RequestParam("bucketName") String bucketName,
                                                   @RequestParam("fileName") String fileName) {
        try {
            String content = awsService.getS3FileContent(bucketName, fileName);
            return ResponseEntity.ok(content);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read file content.");
        }
    }

    @GetMapping("/listS3Files")
    public ResponseEntity<List<Asset>> listS3Files(@RequestParam("bucketName") String bucketName) {
        try {
            List<Asset> files = awsService.getS3Files(bucketName);
            return ResponseEntity.ok(files);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/downloadS3File")
    public ResponseEntity<ByteArrayResource> downloadS3File(@RequestParam("bucketName") String bucketName,
                                                            @RequestParam("filePath") String filePath,
                                                            @RequestParam("fileName") String fileName) {
        try {
            byte[] data = awsService.downloadFile(bucketName, fileName);
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                    .contentLength(data.length)
                    .header("Content-Type", "application/octet-stream")
                    .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                    .body(resource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteObject")
    public ResponseEntity<String> deleteObject(@RequestParam("bucketName") String bucketName,
                                               @RequestParam("fileName") String fileName) {
        try {
            awsService.deleteObject(bucketName, fileName);
            return ResponseEntity.ok("File deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete file.");
        }
    }

    @PutMapping("/moveFile")
    public ResponseEntity<String> moveFile(@RequestParam("bucketName") String bucketName,
                                           @RequestParam("fileKey") String fileKey,
                                           @RequestParam("fileNameDest") String fileNameDest) {
        try {
            awsService.moveObject(bucketName, fileKey, fileNameDest);
            return ResponseEntity.ok("File moved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to move file.");
        }
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("bucketName") String bucketName,
                                             @RequestParam("filePath") String filePath,
                                             @RequestParam("file") MultipartFile file) {
        try {
            String message = awsService.uploadFile(bucketName, filePath, file);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
        }
    }
}
