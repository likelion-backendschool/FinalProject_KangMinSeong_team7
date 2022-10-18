package com.example.eBook.domain.postKeyword.repository;

import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostKeywordRepository extends JpaRepository<PostKeyword, Long> {

    Optional<PostKeyword> findByContent(String content);
}
