package com.example.ebook.domain.postkeyword.repository;

import com.example.ebook.domain.postkeyword.entity.PostKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostKeywordRepository extends JpaRepository<PostKeyword, Long> {

    Optional<PostKeyword> findByContent(String content);
}
