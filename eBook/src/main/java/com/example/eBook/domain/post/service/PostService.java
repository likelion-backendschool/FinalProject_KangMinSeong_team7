package com.example.eBook.domain.post.service;

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
import com.example.eBook.global.util.markdown.MarkdownUtil;
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
    private final MarkdownUtil markdownUtil;

    @Transactional(readOnly = true)
    public List<PostDto> findRecentTop100() {
        return PostMapper.INSTANCE.entitiesToPostDtos(postRepository.findTop100ByOrderByCreateDateDesc());
    }

    @Transactional(readOnly = true)
    public List<PostDto> findAllByMember(Member member) {
        return PostMapper.INSTANCE.entitiesToPostDtos(postRepository.findAllByMember(member));
    }

    public Post save(String username, PostWriteForm postWriteForm) {
        Post post = PostMapper.INSTANCE.postWriteFormToEntity(postWriteForm);
        Member member = memberService.findByUsername(username);

        post.updateMember(member);
        post.updateContentHtml(markdownUtil.markdown(post.getContent()));
        Post savedPost = postRepository.saveAndFlush(post);

        List<PostKeyword> postKeywords = postKeywordService.save(postWriteForm.getKeywords());
        postHashTagService.save(member, savedPost, postKeywords);

        return savedPost;
    }

    @Transactional(readOnly = true)
    public PostDetailDto getPostDetail(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException("해당 글은 존재하지 않습니다."));

        PostDetailDto postDetailDto = PostMapper.INSTANCE.entityToPostDetailDto(post);
        postDetailDto.setPostKeywords(postHashTagService.findAllPostKeywordByPost(post));

        return postDetailDto;
    }

    @Transactional(readOnly = true)
    public PostModifyForm getPostModifyForm(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException("해당 글은 존재하지 않습니다."));

        PostModifyForm postModifyForm = PostMapper.INSTANCE.entityToPostModifyForm(post);

        String postKeywordContents = postHashTagService.findAllPostKeywordByPost(post)
                .stream()
                .map(p -> p.getContent())
                .collect(Collectors.joining(" "));

        postModifyForm.setPostKeywordContents(postKeywordContents);
        return postModifyForm;
    }

    public void modify(Long postId, PostModifyForm postModifyForm) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException("해당 글은 존재하지 않습니다."));

        post.updateSubjectAndContent(postModifyForm.getSubject(), postModifyForm.getContent(),
                markdownUtil.markdown(postModifyForm.getContent()));

        postHashTagService.modify(post, postModifyForm.getPostKeywordContents());
    }

    public void delete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException("해당 글은 존재하지 않습니다."));

        postRepository.delete(post);
        postHashTagService.delete(post);
    }
}
