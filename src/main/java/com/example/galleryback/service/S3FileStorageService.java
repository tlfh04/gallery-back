package com.example.galleryback.service;


import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@Primary
@ConditionalOnProperty(name = "cloud.aws.s3.bucket")
@RequiredArgsConstructor
public class S3FileStorageService implements FileStorageService {

    private final S3Client s3Client;

    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String upload(MultipartFile file) {
        try{
            String key = "uploads/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();
            s3Client.putObject(request, RequestBody.fromInputStream(
                    file.getInputStream(), file.getSize()
            ));
            return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String imageUrl) {
        String prefix = "https://" + bucket + ".s3." + region + ".amazonaws.com/";
        String key = imageUrl.replace(prefix,"");

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        s3Client.deleteObject(request);
    }
    /// ddd
}
