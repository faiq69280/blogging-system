package com.example.article_api.service;

import com.example.article_api.async.events.PostLikeEvent;
import com.example.article_api.caching.LookupMemory;
import com.example.article_api.domain.PostDomainModel;
import com.example.article_api.domain.PostMetadata;
import com.example.article_api.dto.PostCreateDTO;
import com.example.article_api.dto.PostDTO;
import com.example.article_api.exception.NotFoundException;
import com.example.article_api.exception.UnsupportedMediaException;
import com.example.article_api.model.*;
import com.example.article_api.repository.PostLikeRepository;
import com.example.article_api.repository.PostMetadataRepositoryFacade;
import com.example.article_api.repository.PostRepository;
import com.example.article_api.repository.TagRepository;
import com.example.article_api.utils.CommonObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    MediaStorageService mediaStorageService;

    @Autowired
    PostLikeRepository postLikeRepository;


    @Autowired
    PostMetadataRepositoryFacade postMetadataRepositoryFacade;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    /**
     * @param postCreateDTO
     * @param multipartFile
     * @param userId
     * @return
     * @throws NotFoundException
     * @throws UnsupportedMediaException
     * @throws IOException
     */
    @Transactional
    public PostDTO createPost(PostCreateDTO postCreateDTO, MultipartFile multipartFile, String userId) throws NotFoundException, UnsupportedMediaException, IOException {
        List<Tag> tags = findTags(postCreateDTO.tags());
        Post.PostMediaType type = Post.PostMediaType.fromContentType(multipartFile.getContentType());

        Post post = Post.builder().caption(postCreateDTO.caption())
                .userId(userId)
                .tags(tags)
                .mediaType(type)
                .build();

        String url = null;
        if (multipartFile != null && !multipartFile.isEmpty()) {
            UploadedMedia uploadedMedia = mediaStorageService.upload(multipartFile);
            post.setMediaUrl(uploadedMedia.getUrl());
            url = mediaStorageService.getPreSignedUrl(uploadedMedia.getUrl());
        }

        Post savedPost = postRepository.save(post);
        return new PostDTO(savedPost.getId(), savedPost.getCaption(), postCreateDTO.tags(), url);
    }

    /**
     * @param postDTO
     * @param multipartFileToUpdate
     * @return
     * @throws NotFoundException
     * @throws UnsupportedMediaException
     * @throws IOException
     */
    @Transactional
    public PostDTO editPost(PostDTO postDTO, MultipartFile multipartFileToUpdate) throws NotFoundException, UnsupportedMediaException, IOException {
        Post postFound = CommonObjectUtils.requirePresent(postRepository.findById(postDTO.postId()), "Post not found");

        if (postDTO.caption() != null) {
            postFound.setCaption(postDTO.caption());
        }

        if (postDTO.tags() != null) {
            List<Tag> tagsFound = findTags(postDTO.tags());
            postFound.clearTags();
            postFound.setTags(tagsFound);
        }

        String preSignedUrl = null;
        if (multipartFileToUpdate != null && !multipartFileToUpdate.isEmpty()) {
            if (StringUtils.isBlank(postFound.getMediaUrl())) {
                mediaStorageService.deleteFile(postFound.getMediaUrl());
            }
            UploadedMedia uploadedMedia = mediaStorageService.upload(multipartFileToUpdate);
            postFound.setMediaUrl(uploadedMedia.getUrl());
            postFound.setMediaType(Post.PostMediaType.fromContentType(multipartFileToUpdate.getContentType()));
            preSignedUrl = mediaStorageService.getPreSignedUrl(uploadedMedia.getUrl());
        }

        Post postUpdated = postRepository.save(postFound);
        return new PostDTO(postUpdated.getId(), postUpdated.getCaption(), postUpdated.getTags().stream().map(Tag::getTagText).toList(), preSignedUrl);
    }

    /**
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     * @throws NotFoundException
     */
    public Page<PostDTO> findPostsByUserId(String userId, int page, int pageSize) throws NotFoundException {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Post> posts = postRepository.findByUserId(userId, pageable);
        return posts.map(this::convertToDTO);
    }

    /**
     * @param userId
     * @param postId
     * @return
     * @throws NotFoundException
     */
    @Transactional
    public int likePost(String userId, String postId, String userName) throws NotFoundException {
        Post foundPost = CommonObjectUtils.requirePresent(postRepository.findById(postId), "Post not found against id: %s".formatted(postId));

        PostLikes postLikes = new PostLikes();
        postLikes.setId(new PostLikes.PostLikeId(userId, postId));
        postLikes.setPost(foundPost);
        postLikeRepository.save(postLikes);

        int likesCount = postLikeRepository.countByIdPostId(postId);
        applicationEventPublisher.publishEvent(new PostLikeEvent(
                new PostLikeEvent.PostUser(userId, userName),
                foundPost.getUserId(),
                foundPost.getId(),
                likesCount)
        );

        return likesCount;
    }

    @LookupMemory(key = "postId")
    public PostDomainModel findPostWithMetadataById(String postId) throws NotFoundException {
        Post postFound = CommonObjectUtils.requirePresent(postRepository.findById(postId), "couldnt find post with id:%s".formatted(postId));
        PostMetadata postMetadata = postMetadataRepositoryFacade.findMetadataByPostId(postId);

        PostDomainModel postDomainModel = PostDomainModel.builder().postMetadata(postMetadata)
                .postId(postId)
                .name(postFound.getCaption())
                .tags(postFound.getTags().stream().map(Tag::getTagText).toList())
                .url(postFound.getMediaUrl())
                .build();

        return postDomainModel;
    }

    /**
     *
     * @param post
     * @return
     */
    private PostDTO convertToDTO(Post post) {
        return PostDTO.builder().postId(post.getId())
                .caption(post.getCaption())
                .url(mediaStorageService.getPreSignedUrl(post.getMediaUrl()))
                .tags(post.getTags().stream().map(Tag::getTagText).toList())
                .build();
    }

    List<Tag> findTags(List<String> tags) {
        return tagRepository.findByTagTextIn(tags);
    }
}
