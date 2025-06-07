package com.example.tienda.repository;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.example.tienda.model.Asset;
import java.io.File;
import java.io.IOException;
import java.util.List;

// define metodos para interactuar con un almacenamiento S3, como listar objetos en un bucket, obtener objetos individuales, 
// descargar archivos, mover objetos entre ubicaciones, eliminar objetos y subir nuevos archivos

public interface S3Repository {
    List<Asset> listObjectsInBucket(String bucket);

    S3ObjectInputStream getoObject(String bucketName, String fileName) throws IOException;

    byte[] downloadFile(String bucketName, String fileName) throws IOException;

    void moveObject(String bucketName, String fileKey, String destinationFileKey);

    void deleteObject(String bucketName, String fileKey);

    String uploadFile(String bucketName, String fileName, File fileObj);
}
