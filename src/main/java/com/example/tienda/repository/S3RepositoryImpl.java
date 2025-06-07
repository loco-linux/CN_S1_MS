package com.example.tienda.repository;

import com.example.tienda.model.Asset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
//import software.amazon.awssdk.core.sync.ResponseInputStream;
import software.amazon.awssdk.core.ResponseInputStream;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class S3RepositoryImpl implements S3Repository {

    private static final Logger log = LoggerFactory.getLogger(S3RepositoryImpl.class);

    private final S3Client s3Client;

    @Autowired
    public S3RepositoryImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public List<Asset> listObjectsInBucket(String bucket) {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucket)
                .build();
        ListObjectsV2Response response = s3Client.listObjectsV2(listRequest);

        return response.contents().stream()
                .map(s3Object -> mapS3ToObject(bucket, s3Object.key()))
                .collect(Collectors.toList());
    }

    private Asset mapS3ToObject(String bucket, String key) {
        HeadObjectRequest headRequest = HeadObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        HeadObjectResponse headResponse = s3Client.headObject(headRequest);

        return Asset.builder()
            .name(headResponse.metadata().get("name"))
            .key(key)
            .url(s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(key)).toExternalForm())
            .build();
    }

    @Override
    public InputStream getObject(String bucketName, String fileName) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest);
        return response;
    }

    @Override
    public byte[] downloadFile(String bucketName, String fileName) throws IOException {
        try (InputStream inputStream = getObject(bucketName, fileName)) {
            return inputStream.readAllBytes();
        }
    }

    @Override
    public void moveObject(String bucketName, String fileKey, String destinationFileKey) {
        CopyObjectRequest copyRequest = CopyObjectRequest.builder()
                .sourceBucket(bucketName)
                .sourceKey(fileKey)
                .destinationBucket(bucketName)
                .destinationKey(destinationFileKey)
                .build();
        s3Client.copyObject(copyRequest);
        deleteObject(bucketName, fileKey);
    }

    @Override
    public void deleteObject(String bucketName, String fileKey) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .build();
        s3Client.deleteObject(deleteRequest);
    }

    @Override
    public String uploadFile(String bucketName, String fileName, File fileObj) {
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.putObject(putRequest, fileObj.toPath());
        fileObj.delete();
        return "File uploaded: " + fileName;
    }
}
