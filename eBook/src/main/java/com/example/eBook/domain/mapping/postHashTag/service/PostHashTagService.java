package com.example.eBook.domain.mapping.postHashTag.service;

import com.example.eBook.domain.mapping.postHashTag.dto.PostKeywordDto;
import com.example.eBook.domain.mapping.postHashTag.entity.PostHashTag;
import com.example.eBook.domain.mapping.postHashTag.repository.PostHashTagRepository;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.post.entity.Post;
import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import com.example.eBook.domain.postKeyword.service.PostKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostHashTagService {

    private final PostHashTagRepository postHashTagRepository;
    private final PostKeywordService postKeywordService;

    public void save(Member member, Post post, List<PostKeyword> postKeywords) {

        for (PostKeyword postKeyword : postKeywords) {
            postHashTagRepository.save(PostHashTag.builder()
                    .member(member)
                    .post(post)
                    .postKeyword(postKeyword)
                    .build());
        }
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

    public void modify(Post post, String postKeywordContents) {

        postHashTagRepository.deleteAllByPostInQuery(post);

        List<PostKeyword> postKeywords = postKeywordService.save(postKeywordContents);

        for (PostKeyword postKeyword : postKeywords) {
            postHashTagRepository.save(PostHashTag.builder()
                    .member(post.getMember())
                    .post(post)
                    .postKeyword(postKeyword)
                    .build());
        }
    }

    public void delete(Post post) {
        postHashTagRepository.deleteAllByPostInQuery(post);
    }
}
