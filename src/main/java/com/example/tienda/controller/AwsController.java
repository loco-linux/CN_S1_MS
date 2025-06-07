package com.example.tienda.controller;

import com.example.tienda.model.Asset;
import com.example.tienda.service.AwsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // Asumo que es PutMapping por el contexto, la imagen no es clara.
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/s3")
public class AwsController {

    private AwsService awsService;

    // ~ AwsServiceImpl
    @Autowired 
    public AwsController(AwsService awsService) {
        this.awsService = awsService;
    }

    // http://127.0.0.1:8080/s3/getS3FileContent (Count=6 Total=0.20s Max=0.00s)
    @GetMapping("/getS3FileContent")
    public ResponseEntity<String> getS3FileContent(@RequestParam(value = "bucketName") String bucketName, @RequestParam(value = "fileName") String fileName) throws IOException {
        return new ResponseEntity<>(awsService.getS3FileContent(bucketName, fileName), HttpStatus.OK);
    }

    @GetMapping("/listS3Files")
    public ResponseEntity<List<Asset>> listS3Files(@RequestParam(value = "bucketName") String bucketName) throws IOException {
        List<Asset> list = new ArrayList<>();
        HttpStatus status = HttpStatus.OK;
        try {
            list = awsService.getS3Files(bucketName);
        } catch (Exception e) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(list, status);
    }

    // http://127.0.0.1:8080/s3/downloadS3File (Count=5 Total=2.27s Max=0.00s)
    @GetMapping("/downloadS3File")
    public ResponseEntity<ByteArrayResource> downloadS3File(@RequestParam(value = "bucketName") String bucketName, @RequestParam(value = "filePath") String filePath, @RequestParam(value = "fileName") String fileName)
            throws IOException {
        byte[] data = awsService.downloadFile(bucketName, fileName);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    // http://127.0.0.1:8080/s3/deleteObject (Count=4 Total=1.77s Max=0.00s)
    @DeleteMapping("/deleteObject")
    public ResponseEntity<String> deleteObject(@RequestParam(value = "bucketName") String bucketName, @RequestParam(value = "fileName") String fileName) {
        awsService.deleteObject(bucketName, fileName);
        return new ResponseEntity<>("File deleted", HttpStatus.OK);
    }

    // http://127.0.0.1:8080/s3/moveFile (Count=1 Total=0.02s Max=0.00s)
    @PutMapping("/moveFile") // Asumo que es PutMapping por el contexto y la URL.
    public ResponseEntity<String> moveFile(@RequestParam(value = "bucketName") String bucketName,
                                           @RequestParam(value = "fileKey") String fileKey,
                                           @RequestParam(value = "fileNameDest") String fileNameDest) { // Corregido el nombre del parámetro según el uso
        awsService.moveObject(bucketName, fileKey, fileNameDest); // Corregido el nombre del parámetro según el uso
        return new ResponseEntity<>("File moved", HttpStatus.OK);
    }

    // http://127.0.0.1:8080/s3/uploadFile (Count=4 Total=1.56s Max=0.00s)
    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "bucketName") String bucketName, @RequestParam(value = "filePath") String filePath,
                                             @RequestParam(value = "file") MultipartFile file) {
        return new ResponseEntity<>(awsService.uploadFile(bucketName, filePath, file), HttpStatus.OK);
    }

}