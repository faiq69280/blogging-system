package com.example.article_api.repository;

import com.example.article_api.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {
    public List<Tag> findByTagTextIn(List<String> tagtexts);
}
