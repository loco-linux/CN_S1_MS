package com.example.tienda.service;

import com.example.tienda.model.Asset;
import com.example.tienda.repository.S3Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class AwsServiceImpl implements AwsService {

    private static final Logger log = LoggerFactory.getLogger(AwsServiceImpl.class);

    private final S3Repository s3Repository;

    @Autowired
    public AwsServiceImpl(S3Repository s3Repository) {
        this.s3Repository = s3Repository;
    }

    @Override
    public List<Asset> getS3Files(String bucket) {
        return s3Repository.listObjectsInBucket(bucket);
    }

    @Override
    public String getS3FileContent(String bucketName, String fileName) throws IOException {
        try (InputStream inputStream = s3Repository.getObject(bucketName, fileName)) {
            return getAsString(inputStream);
        }
    }

    private String getAsString(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    @Override
    public byte[] downloadFile(String bucketName, String fileName) {
        try {
            return s3Repository.downloadFile(bucketName, fileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file", e);
        }
    }
    

    @Override
    public void moveObject(String bucketName, String fileKey, String destinationFileKey) {
        s3Repository.moveObject(bucketName, fileKey, destinationFileKey);
    }

    @Override
    public void deleteObject(String bucketName, String fileKey) {
        s3Repository.deleteObject(bucketName, fileKey);
    }

    @Override
    public String uploadFile(String bucketName, String filePath, MultipartFile file) {
        File fileObj = null;
        try {
            fileObj = convertMultiPartFileToFile(file);
            String generatedFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            s3Repository.uploadFile(bucketName, filePath + "/" + generatedFileName, fileObj);
            return generatedFileName; // Devuelve el nombre del archivo generado
        } catch (IOException e) {
            log.error("Error uploading file to S3", e);
            throw new RuntimeException("Error uploading file", e);
        } finally {
            if (fileObj != null && fileObj.exists()) {
                boolean deleted = fileObj.delete();
                if (!deleted) {
                    log.warn("Temporary file {} was not deleted", fileObj.getAbsolutePath());
                }
            }
        }
    }

    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = File.createTempFile("upload-", file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }
}
