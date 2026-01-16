package com.example.galleryback.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public String upload(MultipartFile file) {
        try{
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadPath, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath,file.getBytes());
            return "/uploads/" + fileName;
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String imageUrl){

        try{
            Path filePath = Paths.get(uploadPath, imageUrl.replace("/uploads/",""));
            Files.deleteIfExists(filePath);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
