package com.example.tienda.repository;

import com.amazonaws.services.*;
import com.amazonaws.util.IOUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.tienda.model.Asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


// implementa la interfaz S3Repository y define metodos especificos para interactuar con AWS S3 como listar objetos en un bucket


@Repository
public class S3RepositoryImpl implements S3Repository {
    
    private AmazonS3 s3Client;

    @Autowired
    public S3RepositoryImpl(AmazonS3 s3Client){
        this.s3Client = s3Client;
    }

    @Override
    public List<Asset> listObjectsInBucket(String bucket){
        List<Asset> items =
            s3Client.listObjetsV2(bucket).getObjectSummaries().stream()
                .parallel()
                .map(S3OBjectSummary::getKey)
                .map(key -> mapS3toObject(bucket, key))
                .collect(Collectors.toList());
        log.info("Found " + items.size() + " objects in the bucket " + bucket);
        return items;
    }

    private Asset mapS3ToObject(String bucket, String key){
        return Asset.builder()
            .name(s3Client.getObjetMetadata(bucket, key).getUserMetaDataOf(key:"name"))
            .key(key)
            .url(s3Client.getUrl(bucket, key))
            .build();
    }

    @Override
    public S3ObjectInputStream getObject(String bucketName, String fileName) throws IOException{
        if(!s3Client.doesBucketExistV2(bucketName)){
            log.error(msg: "No bucket Found");
            return null;
        }
        S3Object s3object = s3Client.getObject(bucketName, fileName);
        return s3object.getObjectContent();
    }

    @Override
    public byte[] downloadFile(String bucketName, String fileName){
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void moveObject(String bucketName, String fileKey, String destinationFileKey){
        CopyObjectRequest copyObjRequest = new CopyObjectRequest(bucketName, fileKey, bucketName);
        s3Client.copyObject(copyObjRequest);
        deleteObject(bucketName, fileKey);
    }


    @Override
    public void deleteObject(String bucketName, String fileKey){
        s3Client.deleteObject(bucketName, fileKey);
    }

    public String uploadFile(String bucketName, String fileName, File fileObj){
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();
        return "File uploaded: " + fileName;
    }



}
