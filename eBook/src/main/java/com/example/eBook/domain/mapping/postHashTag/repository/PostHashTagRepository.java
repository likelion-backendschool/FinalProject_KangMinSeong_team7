package com.example.eBook.domain.mapping.postHashTag.repository;

import com.example.eBook.domain.mapping.postHashTag.entity.PostHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {

    @Query("SELECT p FROM PostHashTag p JOIN FETCH p.postKeyword")
    List<PostHashTag> findAllWithPostKeyword();
}
