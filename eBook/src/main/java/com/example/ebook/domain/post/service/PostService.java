package com.example.ebook.domain.post.service;

import com.example.ebook.domain.mapping.posthashtag.service.PostHashTagService;
import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.member.service.MemberService;
import com.example.ebook.domain.post.dto.PostDetailDto;
import com.example.ebook.domain.post.dto.PostDto;
import com.example.ebook.domain.post.dto.PostModifyForm;
import com.example.ebook.domain.post.dto.PostWriteForm;
import com.example.ebook.domain.post.entity.Post;
import com.example.ebook.domain.post.exception.PostNotFoundException;
import com.example.ebook.domain.post.repository.PostRepository;
import com.example.ebook.domain.postkeyword.entity.PostKeyword;
import com.example.ebook.domain.postkeyword.service.PostKeywordService;
import com.example.ebook.global.mapper.PostMapper;
import com.example.ebook.global.util.markdown.MarkdownUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<PostDto> findByPageable(int page) {
        Pageable pageable = PageRequest.of(page, 10);

        return postRepository.findAll(pageable).map(PostMapper.INSTANCE::entityToPostDto);
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
        Post savedPost = postRepository.save(post);

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
