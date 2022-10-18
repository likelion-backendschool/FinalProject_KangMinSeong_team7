package com.example.eBook.domain.post.service;

import com.example.eBook.domain.mapping.postHashTag.service.PostHashTagService;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.service.MemberService;
import com.example.eBook.domain.post.dto.PostDto;
import com.example.eBook.domain.post.dto.PostWriteForm;
import com.example.eBook.domain.post.entity.Post;
import com.example.eBook.domain.post.repository.PostRepository;
import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import com.example.eBook.domain.postKeyword.service.PostKeywordService;
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
    private final MemberService memberService;
    private final PostKeywordService postKeywordService;
    private final PostHashTagService postHashTagService;

    @Transactional(readOnly = true)
    public List<PostDto> findAll() {
        return PostMapper.INSTANCE.entitiesToPostDtos(postRepository.findAll());
    }

    public void save(String username, PostWriteForm postWriteForm) {
        Post post = PostMapper.INSTANCE.postWriteFormToEntity(postWriteForm);
        Member member = memberService.findByUsername(username);

        post.updateMember(member);
        postRepository.saveAndFlush(post);

        List<PostKeyword> postKeywords = postKeywordService.save(postWriteForm.getKeywords());
        postHashTagService.save(member, post, postKeywords);
    }
}
