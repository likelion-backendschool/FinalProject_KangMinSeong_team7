package com.example.eBook.domain.post;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.repository.MemberRepository;
import com.example.eBook.domain.post.controller.PostController;
import com.example.eBook.domain.post.dto.PostDetailDto;
import com.example.eBook.domain.post.dto.PostDto;
import com.example.eBook.domain.post.dto.PostModifyForm;
import com.example.eBook.domain.post.entity.Post;
import com.example.eBook.domain.post.repository.PostRepository;
import com.example.eBook.domain.post.service.PostService;
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

import javax.persistence.EntityManager;
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
    private EntityManager entityManager;

    @BeforeEach
    void beforeEach() {
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now()));

        List<Post> postList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            postList.add(new Post((long) i, member, "subject %s".formatted(i),
                    "content %s".formatted(i), "contentHtml %s".formatted(i)));
        }

        postRepository.saveAll(postList);
    }

    @AfterEach
    void afterEach() {
        this.entityManager
                .createNativeQuery("ALTER TABLE post ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
    }

    @Test
    @DisplayName("홈-최근글100개보여주기")
    @WithAnonymousUser
    void showRecentPost() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("home/home"))
                .andExpect(model().attributeExists("postList"))
                .andDo(print());

        List<PostDto> postDtoList = (List<PostDto>) resultActions.andReturn().getModelAndView().getModel().get("postList");
        assertThat(postDtoList.size()).isEqualTo(10);
    }


    @Test
    @DisplayName("글목록조회")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void showPostList() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/post/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("post/list_post"))
                .andExpect(model().attributeExists("postList"))
                .andDo(print());

        List<PostDto> postDtoList = (List<PostDto>) resultActions.andReturn().getModelAndView().getModel().get("postList");
        assertThat(postDtoList.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("글작성폼_보여주기")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void showWriteForm() throws Exception {
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
        mockMvc.perform(post("/post/write")
                        .param("subject", "new subject")
                        .param("content", "new content")
                        .param("keywords", "#new1 #new2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("save"))
                .andExpect(redirectedUrl("/post/list"));

        assertThat(postService.findAll().size()).isEqualTo(11);
    }

    @Test
    @DisplayName("글상세보기")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void showPostDetail() throws Exception {


        ResultActions resultActions = mockMvc.perform(get("/post/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("post/detail_post"))
                .andExpect(model().attributeExists("postDetailDto"))
                .andDo(print());

        PostDetailDto result = (PostDetailDto) resultActions.andReturn().getModelAndView().getModel().get("postDetailDto");

        assertThat(result.getSubject()).isEqualTo("subject 1");
        assertThat(result.getContent()).isEqualTo("content 1");
        assertThat(result.getWriter()).isEqualTo("test_username");
    }

    @Test
    @DisplayName("글수정폼_보여주기")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void showModifyForm() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/post/1/modify"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("post/modify_post"))
                .andExpect(model().attributeExists("postModifyForm"))
                .andDo(print());

        PostModifyForm result = (PostModifyForm) resultActions.andReturn().getModelAndView().getModel().get("postModifyForm");

        assertThat(result.getSubject()).isEqualTo("subject 1");
        assertThat(result.getContent()).isEqualTo("content 1");
    }

    @Test
    @DisplayName("글수정")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void modify() throws Exception {
        mockMvc.perform(post("/post/1/modify")
                        .param("subject", "modify subject")
                        .param("content", "modify content")
                        .param("postKeywordContents", "#key1 #key2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(redirectedUrlPattern("/post/**"));

        Post findPost = postRepository.findById(1L).orElseThrow();

        assertThat(findPost.getSubject()).isEqualTo("modify subject");
        assertThat(findPost.getContent()).isEqualTo("modify content");
    }

    @Test
    @DisplayName("글삭제")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void delete() throws Exception {
        mockMvc.perform(get("/post/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(redirectedUrl("/post/list"));

        assertThat(postRepository.findById(1L).isEmpty()).isTrue();
        assertThat(postRepository.findAll().size()).isEqualTo(9);
    }
}
