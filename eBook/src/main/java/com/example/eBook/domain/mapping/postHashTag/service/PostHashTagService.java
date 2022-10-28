package com.example.eBook.domain.mapping.postHashTag.service;

import com.example.eBook.domain.mapping.postHashTag.dto.PostKeywordDto;
import com.example.eBook.domain.mapping.postHashTag.entity.PostHashTag;
import com.example.eBook.domain.mapping.postHashTag.repository.PostHashTagRepository;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.post.dto.PostDto;
import com.example.eBook.domain.post.entity.Post;
import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import com.example.eBook.domain.postKeyword.service.PostKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostHashTagService {

    private final PostHashTagRepository postHashTagRepository;
    private final PostKeywordService postKeywordService;

    public void save(Member member, Post post, List<PostKeyword> postKeywords) {

        List<PostHashTag> postHashTagList = new ArrayList<>();

        for (PostKeyword postKeyword : postKeywords) {
            postHashTagList.add(PostHashTag.builder()
                    .member(member)
                    .post(post)
                    .postKeyword(postKeyword)
                    .build());
        }

        postHashTagRepository.saveAll(postHashTagList);
    }

    @Transactional(readOnly = true)
    public List<PostHashTag> findAllByPost(Post post) {
        return postHashTagRepository.findAllByPost(post);
    }

    @Transactional(readOnly = true)
    public List<PostKeywordDto> findAllPostKeywordByMember(Member member) {
        return postHashTagRepository.findPostKeywordsByMember(member);
    }

    @Transactional(readOnly = true)
    public List<PostKeywordDto> findAllPostKeywordByPost(Post post) {
        return postHashTagRepository.findPostKeywordsByPost(post);
    }

    @Transactional(readOnly = true)
    public List<PostDto> findAllPostByMemberAndKeyword(Member member, String postKeyword) {
        List<Long> postKeywordIds = null;

        if (postKeyword != null) {
            postKeywordIds = Arrays.stream(postKeyword.split(","))
                    .map(pk -> Long.parseLong(pk))
                    .collect(Collectors.toList());
        }

        return postHashTagRepository.findPostsByMemberAndKeyword(member, postKeywordIds);
    }

    public void modify(Post post, String postKeywordContents) {

        postHashTagRepository.deleteAllByPostInQuery(post);

        List<PostKeyword> postKeywords = postKeywordService.save(postKeywordContents);
        List<PostHashTag> postHashTagList = new ArrayList<>();

        for (PostKeyword postKeyword : postKeywords) {
            postHashTagList.add(PostHashTag.builder()
                    .member(post.getMember())
                    .post(post)
                    .postKeyword(postKeyword)
                    .build());
        }

        postHashTagRepository.saveAll(postHashTagList);
    }

    public void delete(Post post) {
        postHashTagRepository.deleteAllByPostInQuery(post);
    }
}
