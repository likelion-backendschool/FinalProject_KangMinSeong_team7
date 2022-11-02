package com.example.eBook.domain.post.repository;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findTop100ByOrderByCreateDateDesc();

    List<Post> findAllByMember(Member member);
}