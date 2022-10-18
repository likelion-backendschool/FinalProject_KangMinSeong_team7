package com.example.eBook.domain.mapping.postHashTag.service;

import com.example.eBook.domain.mapping.postHashTag.entity.PostHashTag;
import com.example.eBook.domain.mapping.postHashTag.repository.PostHashTagRepository;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.post.entity.Post;
import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostHashTagService {

    private final PostHashTagRepository postHashTagRepository;

    public void save(Member member, Post post, List<PostKeyword> postKeywords) {

        for (PostKeyword postKeyword : postKeywords) {
            postHashTagRepository.save(PostHashTag.builder()
                    .member(member)
                    .post(post)
                    .postKeyword(postKeyword)
                    .build());
        }
    }
}
