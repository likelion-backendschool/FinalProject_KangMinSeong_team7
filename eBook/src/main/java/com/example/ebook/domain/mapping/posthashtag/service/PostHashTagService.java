package com.example.ebook.domain.mapping.posthashtag.service;

import com.example.ebook.domain.mapping.posthashtag.dto.PostKeywordDto;
import com.example.ebook.domain.mapping.posthashtag.entity.PostHashTag;
import com.example.ebook.domain.mapping.posthashtag.repository.PostHashTagRepository;
import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.post.dto.PostDto;
import com.example.ebook.domain.post.entity.Post;
import com.example.ebook.domain.postkeyword.entity.PostKeyword;
import com.example.ebook.domain.postkeyword.service.PostKeywordService;
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
                    .map(Long::parseLong)
                    .toList();
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
