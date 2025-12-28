package com.example.article_api.repository;

import com.example.article_api.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    @EntityGraph(attributePaths = "tags")
    Page<Post> findByUserId(String userId, Pageable pageable);
}
