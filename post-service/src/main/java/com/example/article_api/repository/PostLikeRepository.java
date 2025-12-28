package com.example.article_api.repository;


import com.example.article_api.model.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLikes, PostLikes.PostLikeId> {
    public int countByIdPostId(String postId);
}
