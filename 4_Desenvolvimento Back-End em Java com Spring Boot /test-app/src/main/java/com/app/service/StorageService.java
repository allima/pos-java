package com.app.service;


import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StorageService {

    private final ObjectProvider<Storage> storageProvider;
    
    @Value("${spring.cloud.gcp.storage.bucket:comprovantes}")
    private String bucketName;
    
    public String uploadAttachment(String base64Content, String paymentId, String contentType) {
        try {
            byte[] content = Base64.getDecoder().decode(base64Content);
            String objectName = String.format("comprovantes/%s/%s.bin", 
                paymentId, UUID.randomUUID());

            Storage storage = storageProvider.getIfAvailable();
            if (storage == null) {
                String fallbackPath = String.format("gs://%s/%s", bucketName, objectName);
                log.warn("Cliente GCP Storage indisponivel. Retornando caminho simulado: {}", fallbackPath);
                return fallbackPath;
            }
            
            BlobId blobId = BlobId.of(bucketName, objectName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();
            
            storage.create(blobInfo, content);
            log.info("Arquivo enviado para GCS: gs://{}/{}", bucketName, objectName);
            
            return String.format("gs://%s/%s", bucketName, objectName);
        } catch (Exception e) {
            log.error("Erro ao fazer upload para GCS", e);
            throw new RuntimeException("Falha ao armazenar anexo", e);
        }
    }
}