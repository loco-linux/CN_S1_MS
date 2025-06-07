package com.example.tienda.service;

import com.amazonaws.util.StringUtils;
import com.example.tienda.model.Asset;
import com.example.tienda.repository.S3Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class AwsServiceImpl implements AwsService {

    private static final Logger logger = LoggerFactory.getLogger(AwsServiceImpl.class);

    private S3Repository s3Repository;

    // ~ S3RepositoryImpl
    @Autowired
    public AwsServiceImpl(S3Repository s3Repository) {
        this.s3Repository = s3Repository;
    }

    public List<Asset> getS3Files(String bucket) {
        return s3Repository.listObjectsInBucket(bucket);
    }

    public String getS3FileContent(String bucketName, String fileName) throws IOException {
        return getAsString(s3Repository.getObject(bucketName, fileName));
    }

    private static String getAsString(InputStream is) throws IOException {
        if (is == null)
            return "";
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StringUtils.UTF8));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            is.close();
        }
        return sb.toString();
    }

    @Override
    public byte[] downloadFile(String bucketName, String fileName) throws IOException {
        return s3Repository.downloadFile(bucketName, fileName); // You, 18 hours ago • s3
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
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        s3Repository.uploadFile(bucketName, filePath + fileName, fileObj);
        return fileName; //Devuelve el nombre del archivo generado
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }
}

