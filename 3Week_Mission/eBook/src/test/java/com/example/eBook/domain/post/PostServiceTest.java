package com.example.eBook.domain.post;

import com.example.eBook.domain.mapping.postHashTag.repository.PostHashTagRepository;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.repository.MemberRepository;
import com.example.eBook.domain.post.dto.PostDetailDto;
import com.example.eBook.domain.post.dto.PostModifyForm;
import com.example.eBook.domain.post.dto.PostWriteForm;
import com.example.eBook.domain.post.entity.Post;
import com.example.eBook.domain.post.repository.PostRepository;
import com.example.eBook.domain.post.service.PostService;
import com.example.eBook.domain.postKeyword.repository.PostKeywordRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
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

@SpringBootTest
@Slf4j
@Transactional
@ActiveProfiles("test")
public class PostServiceTest {

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

    @BeforeEach
    void beforeEach() {
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));
    }

    @Test
    @DisplayName("홈_최근글100개조회")
    void findRecentTop100() {
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            posts.add(new Post());
        }
        postRepository.saveAll(posts);

        assertThat(postService.findRecentTop100().size()).isEqualTo(100);
    }

    @Test
    @DisplayName("자신이작성한_글목록조회")
    void findAllByMember() {
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            posts.add(new Post());
        }
        postRepository.saveAll(posts);
        postRepository.save(new Post((long) 10L, memberRepository.findByUsername("test_username").orElseThrow(),
                "new subject", "new content", "new contentHtml"));

        assertThat(postService.findAllByMember(memberRepository.findByUsername("test_username").orElseThrow()).size()).isEqualTo(1);
    }

    @Test
    @DisplayName("글작성")
    void save() {
        PostWriteForm postWriteForm = PostWriteForm.builder()
                .subject("new_subject")
                .content("new_content")
                .keywords("#key1 #key2 #key3")
                .build();

        Post testPost = postService.save("test_username", postWriteForm);

        assertThat(postRepository.findById(testPost.getId()).isPresent()).isTrue();
        assertThat(postKeywordRepository.count()).isEqualTo(3);
        assertThat(postHashTagRepository.findAllByPost(testPost).size()).isEqualTo(3);
    }

    @Test
    @DisplayName("글상세보기")
    void getPostDetail() {
        PostWriteForm postWriteForm = PostWriteForm.builder()
                .subject("new_subject")
                .content("new_content")
                .keywords("#key1 #key2 #key3")
                .build();

        Post testPost = postService.save("test_username", postWriteForm);

        PostDetailDto postDetail = postService.getPostDetail(testPost.getId());
        assertThat(postDetail.getSubject()).isEqualTo("new_subject");
        assertThat(postDetail.getWriter()).isEqualTo("test_username");
        assertThat(postDetail.getContent()).isEqualTo("new_content");
        assertThat(postDetail.getPostKeywords().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("글수정폼_보여주기")
    void getPostModifyForm() {
        PostWriteForm postWriteForm = PostWriteForm.builder()
                .subject("new_subject")
                .content("new_content")
                .keywords("#key1 #key2 #key3")
                .build();

        Post testPost = postService.save("test_username", postWriteForm);

        PostModifyForm postModifyForm = postService.getPostModifyForm(testPost.getId());
        assertThat(postModifyForm.getSubject()).isEqualTo("new_subject");
        assertThat(postModifyForm.getContent()).isEqualTo("new_content");
        assertThat(postModifyForm.getPostKeywordContents()).isEqualTo("#key1 #key2 #key3");
    }

    @Test
    @DisplayName("글수정")
    void modify() {
        PostWriteForm postWriteForm = PostWriteForm.builder()
                .subject("new_subject")
                .content("new_content")
                .keywords("#key1 #key2 #key3")
                .build();

        Post testPost = postService.save("test_username", postWriteForm);

        postService.modify(testPost.getId(), new PostModifyForm("modify_subject", "modify_content", "#key1 #key5"));

        Post findPost = postRepository.findById(testPost.getId()).orElseThrow();

        assertThat(findPost.getSubject()).isEqualTo("modify_subject");
        assertThat(findPost.getContent()).isEqualTo("modify_content");
        assertThat(postHashTagRepository.findAllByPost(findPost).size()).isEqualTo(2);
        assertThat(postKeywordRepository.findByContent("#key1").isPresent()).isTrue();
        assertThat(postKeywordRepository.findByContent("#key5").isPresent()).isTrue();
    }

    @Test
    @DisplayName("글삭제")
    void delete() {
        PostWriteForm postWriteForm = PostWriteForm.builder()
                .subject("new_subject")
                .content("new_content")
                .keywords("#key1 #key2 #key3")
                .build();

        Post testPost = postService.save("test_username", postWriteForm);

        assertThat(postRepository.count()).isEqualTo(1);
        assertThat(postHashTagRepository.count()).isEqualTo(3);
        postService.delete(testPost.getId());
        assertThat(postRepository.count()).isEqualTo(0);
        assertThat(postHashTagRepository.count()).isEqualTo(0);
    }
}
