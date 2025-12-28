package com.example.article_api.controller;

import com.example.article_api.domain.PostDomainModel;
import com.example.article_api.dto.PostCreateDTO;
import com.example.article_api.dto.PostDTO;
import com.example.article_api.exception.NotFoundException;
import com.example.article_api.exception.UnsupportedMediaException;
import com.example.article_api.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("post")
public class PostController {
    @Autowired
    PostService postService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PostDTO> createPost(@Valid @RequestPart("payload") PostCreateDTO postCreateDTO,
                                              @RequestPart(value = "media", required = false) MultipartFile file) throws UnsupportedMediaException, NotFoundException, IOException {
        String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(postService.createPost(postCreateDTO, file, userId));
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAnyAuthority('BLOGGER','ADMIN')")
    public ResponseEntity<PostDTO> editPost(@Valid @RequestPart("payload") PostDTO postDTO,
                                            @RequestPart("media") MultipartFile file) throws UnsupportedMediaException, NotFoundException, IOException {
        return ResponseEntity.ok(postService.editPost(postDTO, file));
    }

    @GetMapping("/getPostsForUser")
    @PreAuthorize("hasAnyAuthority('BLOGGER','ADMIN')")
    public ResponseEntity<Page<PostDTO>> getPostsForUser(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int pageSize) throws NotFoundException {
        String currentUserId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(postService.findPostsByUserId(currentUserId, page, pageSize));
    }

    @GetMapping("/getPostWithMetadataById")
    public ResponseEntity<PostDomainModel> getPostWithMetadataById(@RequestParam String postId) throws NotFoundException {
        return ResponseEntity.ok(postService.findPostWithMetadataById(postId));
    }

    @PostMapping("/likePost")
    public ResponseEntity<Integer> likePost(@RequestParam String postId) throws NotFoundException {
        String currentUserId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(postService.likePost(currentUserId, postId, currentUserName));
    }
}
