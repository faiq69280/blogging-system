package com.example.article_api.service;

import com.example.article_api.exception.UnsupportedMediaException;
import com.example.article_api.model.UploadedMedia;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MediaStorageService {
    public UploadedMedia upload(MultipartFile multipartFile) throws IOException, UnsupportedMediaException;
    public String getPreSignedUrl(String key);
    public void deleteFile(String key);
}
