package com.example.eBook.domain.postHashTag;

import com.example.eBook.domain.mapping.postHashTag.dto.PostKeywordDto;
import com.example.eBook.domain.mapping.postHashTag.entity.PostHashTag;
import com.example.eBook.domain.mapping.postHashTag.repository.PostHashTagRepository;
import com.example.eBook.domain.mapping.postHashTag.service.PostHashTagService;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.repository.MemberRepository;
import com.example.eBook.domain.post.entity.Post;
import com.example.eBook.domain.post.repository.PostRepository;
import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import com.example.eBook.domain.postKeyword.repository.PostKeywordRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
@ActiveProfiles("test")
public class PostHashTagServiceTest {

    @Autowired
    PostHashTagService postHashTagService;

    @Autowired
    PostHashTagRepository postHashTagRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostKeywordRepository postKeywordRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("글_해시태그_매핑하기")
    void save() {
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        Post post = postRepository.save(new Post(1L, member, "subject1", "content1", "contentHtml1"));

        PostKeyword postKeyword1 = postKeywordRepository.save(new PostKeyword(1L, "#key1"));
        PostKeyword postKeyword2 = postKeywordRepository.save(new PostKeyword(1L, "#key2"));

        postHashTagService.save(member, post, new ArrayList<>() {{
            add(postKeyword1);
            add(postKeyword2);
        }});

        assertThat(postHashTagRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("글_해시태그_매핑조회_By글")
    void findAllByPost() {
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        Post post = postRepository.save(new Post(1L, member, "subject1", "content1", "contentHtml1"));

        PostKeyword postKeyword1 = postKeywordRepository.save(new PostKeyword(1L, "#key1"));
        PostKeyword postKeyword2 = postKeywordRepository.save(new PostKeyword(1L, "#key2"));

        postHashTagRepository.save(new PostHashTag(1L, post, postKeyword1, member));
        postHashTagRepository.save(new PostHashTag(2L, post, postKeyword2, member));

        assertThat(postHashTagService.findAllByPost(post).size()).isEqualTo(2);
    }
    @Test
    @DisplayName("글_해시태그_해시태그만조회_By멤버")
    void findAllPostKeywordByMember() {
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        Post post = postRepository.save(new Post(1L, member, "subject1", "content1", "contentHtml1"));

        PostKeyword postKeyword1 = postKeywordRepository.save(new PostKeyword(1L, "#key1"));
        PostKeyword postKeyword2 = postKeywordRepository.save(new PostKeyword(1L, "#key2"));

        postHashTagRepository.save(new PostHashTag(1L, post, postKeyword1, member));
        postHashTagRepository.save(new PostHashTag(2L, post, postKeyword2, member));

        assertThat(postHashTagService.findAllPostKeywordByMember(member).size()).isEqualTo(2);
        assertThat(postHashTagService.findAllPostKeywordByMember(member).get(0) instanceof PostKeywordDto).isTrue();
    }

    @Test
    @DisplayName("글_해시태그_해시태그만조회_By글")
    void findAllPostKeywordByPost() {
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        Post post = postRepository.save(new Post(1L, member, "subject1", "content1", "contentHtml1"));

        PostKeyword postKeyword1 = postKeywordRepository.save(new PostKeyword(1L, "#key1"));
        PostKeyword postKeyword2 = postKeywordRepository.save(new PostKeyword(1L, "#key2"));

        postHashTagRepository.save(new PostHashTag(1L, post, postKeyword1, member));
        postHashTagRepository.save(new PostHashTag(2L, post, postKeyword2, member));

        assertThat(postHashTagService.findAllPostKeywordByPost(post).size()).isEqualTo(2);
        assertThat(postHashTagService.findAllPostKeywordByPost(post).get(0) instanceof PostKeywordDto).isTrue();
    }

    @Test
    @DisplayName("글_해시태그_글만조회_By멤버+키워드")
    void findAllPostByMemberAndKeyword() {
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        Post post1 = postRepository.save(new Post(1L, member, "subject1", "content1", "contentHtml1"));
        Post post2 = postRepository.save(new Post(2L, member, "subject2", "content2", "contentHtml2"));

        PostKeyword postKeyword1 = postKeywordRepository.save(new PostKeyword(1L, "#key1"));
        PostKeyword postKeyword2 = postKeywordRepository.save(new PostKeyword(2L, "#key2"));

        postHashTagRepository.save(new PostHashTag(1L, post1, postKeyword1, member));
        postHashTagRepository.save(new PostHashTag(2L, post1, postKeyword2, member));
        postHashTagRepository.save(new PostHashTag(3L, post2, postKeyword2, member));

        assertThat(postHashTagService.findAllPostByMemberAndKeyword(member, String.valueOf(postKeyword1.getId())).size()).isEqualTo(1);
        assertThat(postHashTagService.findAllPostByMemberAndKeyword(member, String.valueOf(postKeyword2.getId())).size()).isEqualTo(2);
    }

    @Test
    @DisplayName("글_해시태그_매핑수정")
    void modify() {
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        Post post = postRepository.save(new Post(1L, member, "subject1", "content1", "contentHtml1"));

        PostKeyword postKeyword1 = postKeywordRepository.save(new PostKeyword(1L, "#key1"));
        PostKeyword postKeyword2 = postKeywordRepository.save(new PostKeyword(1L, "#key2"));

        postHashTagRepository.save(new PostHashTag(1L, post, postKeyword1, member));
        postHashTagRepository.save(new PostHashTag(2L, post, postKeyword2, member));

        postHashTagService.modify(post, "#key1 #key4 #key5");

        List<PostHashTag> postHashTags = postHashTagRepository.findAllByPost(post);

        List<String> keywords = postHashTags.stream()
                .map(p -> p.getPostKeyword().getContent())
                .toList();

        assertThat(postHashTags.size()).isEqualTo(3);
        assertThat(keywords.contains("#key1")).isTrue();
        assertThat(keywords.contains("#key4")).isTrue();
        assertThat(keywords.contains("#key5")).isTrue();
    }

    @Test
    @DisplayName("글_해시태그_매핑삭제")
    void delete() {
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        Post post = postRepository.save(new Post(1L, member, "subject1", "content1", "contentHtml1"));

        PostKeyword postKeyword1 = postKeywordRepository.save(new PostKeyword(1L, "#key1"));
        PostKeyword postKeyword2 = postKeywordRepository.save(new PostKeyword(1L, "#key2"));

        postHashTagRepository.save(new PostHashTag(1L, post, postKeyword1, member));
        postHashTagRepository.save(new PostHashTag(2L, post, postKeyword2, member));

        postHashTagService.delete(post);

        assertThat(postHashTagRepository.findAllByPost(post).size()).isEqualTo(0);
    }
}
