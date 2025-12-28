package com.example.article_api.repository;

import com.example.article_api.domain.PostMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PostMetadataRepositoryFacade {

    @Autowired
    PostLikeRepository postLikeRepository;

    @Autowired
    CommentRepository commentRepository;

    public PostMetadata findMetadataByPostId(String postId) {

        int commentsCount = commentRepository.countByPostId(postId);
        int likesCount = postLikeRepository.countByIdPostId(postId);

        return PostMetadata.builder().likesCount(likesCount)
                .commentCount(commentsCount)
                .build();
    }
}
