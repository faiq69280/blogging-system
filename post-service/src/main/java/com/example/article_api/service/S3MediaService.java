package com.example.article_api.service;

import com.example.article_api.exception.UnsupportedMediaException;
import com.example.article_api.model.Post;
import com.example.article_api.model.UploadedMedia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
public class S3MediaService implements MediaStorageService {
    @Autowired
    private S3Client client;

    @Autowired
    S3Presigner s3Presigner;

    @Value("${aws.s3Bucket}")
    private String s3Bucket;

    @Override
    public UploadedMedia upload(MultipartFile multipartFile) throws IOException, UnsupportedMediaException {
        String key = UUID.randomUUID().toString() + "-" + multipartFile.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(s3Bucket)
                .key(key)
                .contentType(multipartFile.getContentType())
                .build();

        client.putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.getBytes()));
        Post.PostMediaType mediaType = Post.PostMediaType.fromContentType(multipartFile.getContentType());

        return UploadedMedia.builder()
                .url(key)
                .size(multipartFile.getSize())
                .mediaType(mediaType)
                .build();
    }

    @Override
    public String getPreSignedUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Bucket)
                .key(key)
                .build();

        PresignedGetObjectRequest presignedGetObject = s3Presigner.presignGetObject(
                b -> b.signatureDuration(Duration.ofMinutes(15))
                        .getObjectRequest(getObjectRequest)
        );

        return presignedGetObject.url().toString();
    }

    @Override
    public void deleteFile(String key) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(s3Bucket)
                .key(key)
                .build();

        client.deleteObject(deleteObjectRequest);
    }
}
