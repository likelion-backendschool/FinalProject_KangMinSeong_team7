package com.example.eBook.domain.mapping.postHashTag.repository;

import com.example.eBook.domain.mapping.postHashTag.entity.PostHashTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {
}
