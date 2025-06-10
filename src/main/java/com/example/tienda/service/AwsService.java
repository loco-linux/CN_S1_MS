package com.example.tienda.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.tienda.model.Asset;


public interface AwsService {
    String getS3FileContent(String fileName) throws IOException;
    List<Asset> getS3Files();
    byte[] downloadFile(String fileName);
    void deleteObject(String fileName);
    void moveObject(String fileKey, String fileNameDest);
    String uploadFile(String path, MultipartFile file) throws IOException;
}

/*public interface AwsService {
    String getS3FileContent(String bucketName, String fileName) throws IOException;
    List<Asset> getS3Files(String bucketName);
    byte[] downloadFile(String bucketName, String fileName);
    void deleteObject(String bucketName, String fileName);
    void moveObject(String bucketName, String fileKey, String fileNameDest);
    //String uploadFile(String bucketName, String filePath, MultipartFile file) throws IOException;
    String uploadFile(String bucketName, MultipartFile file) throws IOException;
}*/
