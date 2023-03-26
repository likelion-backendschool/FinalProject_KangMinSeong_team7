package com.example.ebook.domain.post.repository;

import com.example.ebook.domain.post.entity.Post;

import java.util.List;

public interface CustomPostRepository {

    int batchInsert(List<Post> postList);
}
