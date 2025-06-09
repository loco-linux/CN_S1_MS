package com.example.tienda.service;

import com.example.tienda.model.Asset;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AwsService {
    String getS3FileContent(String bucketName, String fileName) throws IOException;
    List<Asset> getS3Files(String bucketName);
    byte[] downloadFile(String bucketName, String fileName);
    void deleteObject(String bucketName, String fileName);
    void moveObject(String bucketName, String fileKey, String fileNameDest);
    //String uploadFile(String bucketName, String filePath, MultipartFile file) throws IOException;
    String uploadFile(String bucketName, MultipartFile file) throws IOException;
}
