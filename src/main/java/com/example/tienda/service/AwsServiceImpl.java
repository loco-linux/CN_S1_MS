package com.example.tienda.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.tienda.model.Asset;
import com.example.tienda.repository.S3Repository;

@Service
public class AwsServiceImpl implements AwsService {

    private static final Logger log = LoggerFactory.getLogger(AwsServiceImpl.class);

    private final S3Repository s3Repository;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public AwsServiceImpl(S3Repository s3Repository) {
        this.s3Repository = s3Repository;
    }

    @Override
    public List<Asset> getS3Files() {
        return s3Repository.listObjectsInBucket(bucketName);
    }

    @Override
    public String getS3FileContent(String fileName) throws IOException {
        try (InputStream inputStream = s3Repository.getObject(bucketName, fileName)) {
            return getAsString(inputStream);
        }
    }

    private String getAsString(InputStream inputStream) throws IOException {
        if (inputStream == null) return "";
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
    public byte[] downloadFile(String fileName) {
        try {
            return s3Repository.downloadFile(bucketName, fileName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to download file", e);
        }
    }

    @Override
    public void moveObject(String fileKey, String destinationFileKey) {
        s3Repository.moveObject(bucketName, fileKey, destinationFileKey);
    }

    @Override
    public void deleteObject(String fileKey) {
        s3Repository.deleteObject(bucketName, fileKey);
    }

    @Override
    public String uploadFile(String path, MultipartFile file) {
        File fileObj = null;
        try {
            // EFS: /app/efs
            File efsDir = new File("/app/efs");
            if (!efsDir.exists()) {
                boolean created = efsDir.mkdirs();
                if (!created) log.warn("No se pudo crear el directorio EFS.");
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            fileObj = new File(efsDir, fileName);
            file.transferTo(fileObj);

            s3Repository.uploadFile(bucketName, path + "/" + fileName, fileObj);

            return fileName;
        } catch (IOException e) {
            log.error("Error uploading file to S3", e);
            throw new RuntimeException("Error uploading file", e);
        } finally {
            if (fileObj != null && fileObj.exists() && !fileObj.delete()) {
                log.warn("Temporary file {} was not deleted", fileObj.getAbsolutePath());
            }
        }
    }
}
