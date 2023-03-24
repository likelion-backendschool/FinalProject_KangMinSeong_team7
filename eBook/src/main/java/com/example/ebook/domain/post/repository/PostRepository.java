package com.example.ebook.domain.post.repository;

import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findTop100ByOrderByCreateDateDesc();

    List<Post> findAllByMember(Member member);

    Page<Post> findAll(Pageable pageable);
}