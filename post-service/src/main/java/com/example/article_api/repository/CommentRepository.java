package com.example.article_api.repository;

import com.example.article_api.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
    public int countByPostId(String postId);
}
