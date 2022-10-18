package com.example.eBook.domain.mapping.postHashTag.repository;

import com.example.eBook.domain.mapping.postHashTag.entity.PostHashTag;
import com.example.eBook.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {

    @Query("SELECT p FROM PostHashTag p JOIN FETCH p.postKeyword")
    List<PostHashTag> findAllWithPostKeyword();

    @Transactional
    @Modifying
    @Query("delete from PostHashTag p where p.post = :post")
    void deleteAllByPostInQuery(Post post);
}