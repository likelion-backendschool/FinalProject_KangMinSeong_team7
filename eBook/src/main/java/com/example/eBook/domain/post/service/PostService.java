package com.example.eBook.domain.post.service;

import com.example.eBook.domain.post.dto.PostDto;
import com.example.eBook.domain.post.repository.PostRepository;
import com.example.eBook.global.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<PostDto> findAll() {
        return PostMapper.INSTANCE.entitiesToPostDtos(postRepository.findAll());
    }
}
