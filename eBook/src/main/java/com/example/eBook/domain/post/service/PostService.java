package com.example.eBook.domain.post.service;

import com.example.eBook.domain.mapping.postHashTag.entity.PostHashTag;
import com.example.eBook.domain.mapping.postHashTag.service.PostHashTagService;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.service.MemberService;
import com.example.eBook.domain.post.dto.PostDetailDto;
import com.example.eBook.domain.post.dto.PostDto;
import com.example.eBook.domain.post.dto.PostModifyForm;
import com.example.eBook.domain.post.dto.PostWriteForm;
import com.example.eBook.domain.post.entity.Post;
import com.example.eBook.domain.post.exception.PostNotFoundException;
import com.example.eBook.domain.post.repository.PostRepository;
import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import com.example.eBook.domain.postKeyword.service.PostKeywordService;
import com.example.eBook.global.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public PostDetailDto getPostDetail(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException("해당 글은 존재하지 않습니다."));

        PostDetailDto postDetailDto = PostMapper.INSTANCE.entityToPostDetailDto(post);
        postDetailDto.setPostKeywords(postHashTagService.findAllByPost(post)
                .stream()
                .map(PostHashTag::getPostKeyword)
                .collect(Collectors.toList()));

        return postDetailDto;
    }

    @Transactional(readOnly = true)
    public PostModifyForm getPostModifyForm(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException("해당 글은 존재하지 않습니다."));

        PostModifyForm postModifyForm = PostMapper.INSTANCE.entityToPostModifyForm(post);

        String postKeywordContents = postHashTagService.findAllByPost(post)
                .stream()
                .map(p -> p.getPostKeyword().getContent())
                .collect(Collectors.joining(" "));

        postModifyForm.setPostKeywordContents(postKeywordContents);
        return postModifyForm;
    }

    public void modify(Long postId, PostModifyForm postModifyForm) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException("해당 글은 존재하지 않습니다."));

        post.updateSubjectAndContent(postModifyForm.getSubject(), postModifyForm.getContent());

        postHashTagService.modify(post, postModifyForm.getPostKeywordContents());
    }

    public void delete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException("해당 글은 존재하지 않습니다."));

        postRepository.delete(post);
        postHashTagService.delete(post);
    }
}
