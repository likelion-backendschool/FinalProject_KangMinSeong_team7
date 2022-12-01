package com.example.ebook.domain.post;

import com.example.ebook.domain.mapping.posthashtag.entity.PostHashTag;
import com.example.ebook.domain.mapping.posthashtag.repository.PostHashTagRepository;
import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.member.repository.MemberRepository;
import com.example.ebook.domain.post.controller.PostController;
import com.example.ebook.domain.post.dto.PostDetailDto;
import com.example.ebook.domain.post.dto.PostDto;
import com.example.ebook.domain.post.dto.PostModifyForm;
import com.example.ebook.domain.post.entity.Post;
import com.example.ebook.domain.post.repository.PostRepository;
import com.example.ebook.domain.post.service.PostService;
import com.example.ebook.domain.postkeyword.entity.PostKeyword;
import com.example.ebook.domain.postkeyword.repository.PostKeywordRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@Slf4j
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PostHashTagRepository postHashTagRepository;

    @Autowired
    private PostKeywordRepository postKeywordRepository;

    @Test
    @DisplayName("홈-최근글100개보여주기")
    @WithAnonymousUser
    void showRecentPost() throws Exception {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        List<Post> postList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            postList.add(Post.builder()
                    .member(member)
                    .subject("subject %s".formatted(i))
                    .content("content %s".formatted(i))
                    .contentHtml("contentHtml %s".formatted(i))
                    .build());
        }

        postRepository.saveAll(postList);

        // when
        ResultActions resultActions = mockMvc.perform(get("/"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("home/home"))
                .andExpect(model().attributeExists("postList"))
                .andDo(print());

        // then
        List<PostDto> postDtoList = (List<PostDto>) resultActions.andReturn().getModelAndView().getModel().get("postList");
        assertThat(postDtoList.size()).isEqualTo(10);
    }


    @Test
    @DisplayName("자신이작성한_글목록조회")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void showPostList() throws Exception {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        Post post1 = postRepository.save(Post.builder().member(member).subject("new_subject1").content("new_content1").contentHtml("new_contentHtml1").build());
        Post post2 = postRepository.save(Post.builder().member(member).subject("new_subject2").content("new_content2").contentHtml("new_contentHtml2").build());
        Post post3 = postRepository.save(Post.builder().member(member).subject("new_subject3").content("new_content3").contentHtml("new_contentHtml3").build());

        PostKeyword postKeyword1 = postKeywordRepository.save(PostKeyword.builder().content("#key1").build());
        PostKeyword postKeyword2 = postKeywordRepository.save(PostKeyword.builder().content("#key2").build());
        PostKeyword postKeyword3 = postKeywordRepository.save(PostKeyword.builder().content("#key3").build());

        postHashTagRepository.save(PostHashTag.builder().post(post1).postKeyword(postKeyword1).member(member).build());
        postHashTagRepository.save(PostHashTag.builder().post(post2).postKeyword(postKeyword2).member(member).build());
        postHashTagRepository.save(PostHashTag.builder().post(post3).postKeyword(postKeyword3).member(member).build());

        // when then
        ResultActions resultActions = mockMvc.perform(get("/post/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("post/list_post"))
                .andExpect(model().attributeExists("postList", "postKeywordList"))
                .andDo(print());

        List<PostDto> postDtoList = (List<PostDto>) resultActions.andReturn().getModelAndView().getModel().get("postList");
        assertThat(postDtoList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("자신이작성한_글목록조회_By키워드")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void showPostList2() throws Exception {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        Post post1 = postRepository.save(Post.builder().member(member).subject("new_subject1").content("new_content1").contentHtml("new_contentHtml1").build());
        Post post2 = postRepository.save(Post.builder().member(member).subject("new_subject2").content("new_content2").contentHtml("new_contentHtml2").build());
        Post post3 = postRepository.save(Post.builder().member(member).subject("new_subject3").content("new_content3").contentHtml("new_contentHtml3").build());

        PostKeyword postKeyword1 = postKeywordRepository.save(PostKeyword.builder().content("#key1").build());
        PostKeyword postKeyword2 = postKeywordRepository.save(PostKeyword.builder().content("#key2").build());
        PostKeyword postKeyword3 = postKeywordRepository.save(PostKeyword.builder().content("#key3").build());

        postHashTagRepository.save(PostHashTag.builder().post(post1).postKeyword(postKeyword1).member(member).build());
        postHashTagRepository.save(PostHashTag.builder().post(post2).postKeyword(postKeyword2).member(member).build());
        postHashTagRepository.save(PostHashTag.builder().post(post3).postKeyword(postKeyword3).member(member).build());

        // when then
        ResultActions resultActions = mockMvc.perform(get("/post/list?keyword=%s,%s".formatted(postKeyword1.getId(), postKeyword2.getId())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("post/list_post"))
                .andExpect(model().attributeExists("postList", "postKeywordList"))
                .andDo(print());

        List<PostDto> postDtoList = (List<PostDto>) resultActions.andReturn().getModelAndView().getModel().get("postList");
        assertThat(postDtoList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("글작성폼_보여주기")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void showWriteForm() throws Exception {

        // when then
        mockMvc.perform(get("/post/write"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("post/write_post"))
                .andExpect(model().attributeExists("postWriteForm"))
                .andDo(print());
    }

    @Test
    @DisplayName("글작성")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void save() throws Exception {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        // when then
        mockMvc.perform(post("/post/write")
                        .param("subject", "new subject")
                        .param("content", "new content")
                        .param("keywords", "#new1 #new2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("save"))
                .andExpect(redirectedUrl("/post/list"));

        assertThat(postService.findAllByMember(memberRepository.findByUsername("test_username").orElseThrow()).size()).isEqualTo(1);
    }

    @Test
    @DisplayName("글상세보기")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void showPostDetail() throws Exception {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));
        Post post = postRepository.save(new Post(1L, member, "new_subject", "new_content", "new_contentHtml"));

        // when then
        ResultActions resultActions = mockMvc.perform(get("/post/%s".formatted(post.getId())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("post/detail_post"))
                .andExpect(model().attributeExists("postDetailDto"))
                .andDo(print());

        PostDetailDto result = (PostDetailDto) resultActions.andReturn().getModelAndView().getModel().get("postDetailDto");

        assertThat(result.getSubject()).isEqualTo("new_subject");
        assertThat(result.getContent()).isEqualTo("new_content");
        assertThat(result.getWriter()).isEqualTo("test_username");
    }

    @Test
    @DisplayName("글수정폼_보여주기")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void showModifyForm() throws Exception {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));
        Post post = postRepository.save(Post.builder().member(member).subject("new_subject").content("new_content").contentHtml("new_contentHtml").build());


        // when then
        ResultActions resultActions = mockMvc.perform(get("/post/%s/modify".formatted(post.getId())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("post/modify_post"))
                .andExpect(model().attributeExists("postModifyForm"))
                .andDo(print());

        PostModifyForm result = (PostModifyForm) resultActions.andReturn().getModelAndView().getModel().get("postModifyForm");

        assertThat(result.getSubject()).isEqualTo("new_subject");
        assertThat(result.getContent()).isEqualTo("new_content");
    }

    @Test
    @DisplayName("글수정")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void modify() throws Exception {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));
        Post post = postRepository.save(Post.builder().member(member).subject("new_subject").content("new_content").contentHtml("new_contentHtml").build());

        // when then
        mockMvc.perform(post("/post/%s/modify".formatted(post.getId()))
                        .param("subject", "modify subject")
                        .param("content", "modify content")
                        .param("postKeywordContents", "#key1 #key2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(redirectedUrlPattern("/post/**"));

        Post findPost = postRepository.findById(post.getId()).orElseThrow();

        assertThat(findPost.getSubject()).isEqualTo("modify subject");
        assertThat(findPost.getContent()).isEqualTo("modify content");
    }

    @Test
    @DisplayName("글삭제")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void delete() throws Exception {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));
        Post post = postRepository.save(Post.builder().member(member).subject("new_subject").content("new_content").contentHtml("new_contentHtml").build());

        // when then
        mockMvc.perform(get("/post/%s/delete".formatted(post.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(redirectedUrl("/post/list"));

        assertThat(postRepository.findById(1L).isEmpty()).isTrue();
        assertThat(postRepository.findAll().size()).isEqualTo(0);
    }
}

