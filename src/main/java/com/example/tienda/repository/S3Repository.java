package com.example.tienda.repository;

import com.example.tienda.model.Asset;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

// Define metodos para interactuar con un almacenamiento S3.
public interface S3Repository {
    List<Asset> listObjectsInBucket(String bucket);

    InputStream getObject(String bucketName, String fileName) throws IOException;

    byte[] downloadFile(String bucketName, String fileName) throws IOException;

    void moveObject(String bucketName, String fileKey, String destinationFileKey);

    void deleteObject(String bucketName, String fileKey);

    String uploadFile(String bucketName, String fileName, File fileObj);
}
