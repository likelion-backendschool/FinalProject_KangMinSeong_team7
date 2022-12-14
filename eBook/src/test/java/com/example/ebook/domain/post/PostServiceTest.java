package com.example.ebook.domain.post;

import com.example.ebook.domain.mapping.posthashtag.repository.PostHashTagRepository;
import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.member.repository.MemberRepository;
import com.example.ebook.domain.post.dto.PostDetailDto;
import com.example.ebook.domain.post.dto.PostModifyForm;
import com.example.ebook.domain.post.dto.PostWriteForm;
import com.example.ebook.domain.post.entity.Post;
import com.example.ebook.domain.post.repository.PostRepository;
import com.example.ebook.domain.post.service.PostService;
import com.example.ebook.domain.postkeyword.repository.PostKeywordRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
@ActiveProfiles("test")
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostKeywordRepository postKeywordRepository;

    @Autowired
    private PostHashTagRepository postHashTagRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("홈_최근글100개조회")
    void findRecentTop100() {

        // given
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            posts.add(new Post());
        }
        postRepository.saveAll(posts);

        // when
        int size = postService.findRecentTop100().size();

        // then
        assertThat(size).isEqualTo(100);
    }

    @Test
    @DisplayName("자신이작성한_글목록조회")
    void findAllByMember() {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            posts.add(new Post());
        }
        postRepository.saveAll(posts);
        postRepository.save(new Post((long) 10L, memberRepository.findByUsername("test_username").orElseThrow(),
                "new subject", "new content", "new contentHtml"));

        // when
        int size = postService.findAllByMember(member).size();

        // then
        assertThat(size).isEqualTo(1);
    }

    @Test
    @DisplayName("글작성")
    void save() {

        // given
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        PostWriteForm postWriteForm = PostWriteForm.builder()
                .subject("new_subject")
                .content("new_content")
                .keywords("#key1 #key2 #key3")
                .build();

        // when
        Post testPost = postService.save("test_username", postWriteForm);

        // then
        assertAll(
                () -> assertThat(postRepository.findById(testPost.getId())).isPresent(),
                () -> assertThat(postKeywordRepository.count()).isEqualTo(3),
                () -> assertThat(postHashTagRepository.findAllByPost(testPost)).hasSize(3)
        );
    }

    @Test
    @DisplayName("글상세보기")
    void getPostDetail() {

        // when
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        PostWriteForm postWriteForm = PostWriteForm.builder()
                .subject("new_subject")
                .content("new_content")
                .keywords("#key1 #key2 #key3")
                .build();

        // then
        Post testPost = postService.save("test_username", postWriteForm);
        PostDetailDto postDetail = postService.getPostDetail(testPost.getId());

        // then
        assertAll(
                () -> assertThat(postDetail.getSubject()).isEqualTo("new_subject"),
                () -> assertThat(postDetail.getWriter()).isEqualTo("test_username"),
                () -> assertThat(postDetail.getContent()).isEqualTo("new_content"),
                () -> assertThat(postDetail.getPostKeywords()).hasSize(3)
        );
    }

    @Test
    @DisplayName("글수정폼_보여주기")
    void getPostModifyForm() {

        // given
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        PostWriteForm postWriteForm = PostWriteForm.builder()
                .subject("new_subject")
                .content("new_content")
                .keywords("#key1 #key2 #key3")
                .build();

        // when
        Post testPost = postService.save("test_username", postWriteForm);

        // then
        PostModifyForm postModifyForm = postService.getPostModifyForm(testPost.getId());
        assertAll(
                () -> assertThat(postModifyForm.getSubject()).isEqualTo("new_subject"),
                () -> assertThat(postModifyForm.getContent()).isEqualTo("new_content"),
                () -> assertThat(postModifyForm.getPostKeywordContents()).isEqualTo("#key1 #key2 #key3")
        );
    }

    @Test
    @DisplayName("글수정")
    void modify() {

        // given
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        PostWriteForm postWriteForm = PostWriteForm.builder()
                .subject("new_subject")
                .content("new_content")
                .keywords("#key1 #key2 #key3")
                .build();

        Post testPost = postService.save("test_username", postWriteForm);

        // when
        postService.modify(testPost.getId(), new PostModifyForm("modify_subject", "modify_content", "#key1 #key5"));

        // then
        Post findPost = postRepository.findById(testPost.getId()).orElseThrow();

        assertAll(
                () -> assertThat(findPost.getSubject()).isEqualTo("modify_subject"),
                () -> assertThat(findPost.getContent()).isEqualTo("modify_content"),
                () -> assertThat(postHashTagRepository.findAllByPost(findPost)).hasSize(2),
                () -> assertThat(postKeywordRepository.findByContent("#key1")).isPresent(),
                () -> assertThat(postKeywordRepository.findByContent("#key5")).isPresent()
        );
    }

    @Test
    @DisplayName("글삭제")
    void delete() {

        // given
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        PostWriteForm postWriteForm = PostWriteForm.builder()
                .subject("new_subject")
                .content("new_content")
                .keywords("#key1 #key2 #key3")
                .build();

        Post testPost = postService.save("test_username", postWriteForm);

        assertAll(
                () -> assertThat(postRepository.count()).isEqualTo(1),
                () -> assertThat(postHashTagRepository.count()).isEqualTo(3)
        );

        // when
        postService.delete(testPost.getId());

        // then
        assertAll(
                () -> assertThat(postRepository.count()).isZero(),
                () -> assertThat(postHashTagRepository.count()).isZero()
        );
    }
}
