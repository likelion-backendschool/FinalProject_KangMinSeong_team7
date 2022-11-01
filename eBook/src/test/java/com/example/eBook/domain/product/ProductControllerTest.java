package com.example.eBook.domain.product;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.repository.MemberRepository;
import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import com.example.eBook.domain.postKeyword.repository.PostKeywordRepository;
import com.example.eBook.domain.product.controller.ProductController;
import com.example.eBook.domain.product.dto.ProductDetailDto;
import com.example.eBook.domain.product.dto.ProductDto;
import com.example.eBook.domain.product.dto.ProductModifyForm;
import com.example.eBook.domain.product.entity.Product;
import com.example.eBook.domain.product.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PostKeywordRepository postKeywordRepository;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void beforeEach() {
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        PostKeyword postKeyword = postKeywordRepository.save(new PostKeyword(1L, "#keyword1"));
        List<Product> productList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            productList.add(new Product((long) i, member, postKeyword, "subject %s".formatted(i), "description %s".formatted(i), i * 1000));
        }

        productRepository.saveAll(productList);
    }

    @AfterEach
    void afterEach() {
        this.entityManager
                .createNativeQuery("ALTER TABLE product ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
        this.entityManager
                .createNativeQuery("ALTER TABLE post_keyword ALTER COLUMN `id` RESTART WITH 1")
                .executeUpdate();
    }

    @Test
    @DisplayName("상품목록조회")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void showProductList() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/product/list"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("product/list_product"))
                .andExpect(model().attributeExists("productDtoList"))
                .andDo(print());

        List<ProductDto> productDtoList = (List<ProductDto>) resultActions.andReturn().getModelAndView().getModel().get("productDtoList");
        assertThat(productDtoList.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("상품생성폼_보여주기")
    @WithMockUser(username = "test_username", password = "1234", roles = "WRITER")
    void showCreateForm() throws Exception {
        mockMvc.perform(get("/product/create"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("product/create_product"))
                .andExpect(model().attributeExists("productCreateForm"))
                .andDo(print());
    }

    @Test
    @DisplayName("상품생성")
    @WithMockUser(username = "test_username", password = "1234", roles = "WRITER")
    void create() throws Exception {
        mockMvc.perform(post("/product/create")
                        .param("subject", "new subject")
                        .param("price", "10000")
                        .param("description", "new description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("create"))
                .andExpect(redirectedUrl("/product/list"));

        assertThat(productRepository.findAll().size()).isEqualTo(11);
    }

    @Test
    @DisplayName("상품상세정보_보여주기")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void showProductDetail() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/product/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("product/detail_product"))
                .andExpect(model().attributeExists("productDetailDto"))
                .andDo(print());

        ProductDetailDto result = (ProductDetailDto) resultActions.andReturn().getModelAndView().getModel().get("productDetailDto");

        assertThat(result.getSubject()).isEqualTo("subject 1");
        assertThat(result.getDescription()).isEqualTo("description 1");
        assertThat(result.getPrice()).isEqualTo(1000);
        assertThat(result.getWriter()).isEqualTo("test_username");
        assertThat(result.getPostKeywordContent()).isEqualTo(postKeywordRepository.findById(1L).orElseThrow().getContent());
    }

    @Test
    @DisplayName("상품수정폼_보여주기")
    @WithMockUser(username = "test_username", password = "1234", roles = "WRITER")
    void showModifyForm() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/product/1/modify"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("product/modify_product"))
                .andExpect(model().attributeExists("productModifyForm"))
                .andDo(print());

        ProductModifyForm result = (ProductModifyForm) resultActions.andReturn().getModelAndView().getModel().get("productModifyForm");

        assertThat(result.getSubject()).isEqualTo("subject 1");
        assertThat(result.getPrice()).isEqualTo(1000);
        assertThat(result.getDescription()).isEqualTo("description 1");
    }

    @Test
    @DisplayName("상품수정")
    @WithMockUser(username = "test_username", password = "1234", roles = "WRITER")
    void modify() throws Exception {
        mockMvc.perform(post("/product/1/modify")
                        .param("subject", "modify subject")
                        .param("description", "modify description")
                        .param("price", "2000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(redirectedUrlPattern("/product/**"));

        Product findProduct = productRepository.findById(1L).orElseThrow();

        assertThat(findProduct.getSubject()).isEqualTo("modify subject");
        assertThat(findProduct.getDescription()).isEqualTo("modify description");
        assertThat(findProduct.getPrice()).isEqualTo(2000);
    }

    @Test
    @DisplayName("상품삭제")
    @WithMockUser(username = "test_username", password = "1234", roles = "WRITER")
    void delete() throws Exception {
        mockMvc.perform(get("/product/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(redirectedUrl("/product/list"));

        assertThat(productRepository.findById(1L).isEmpty()).isTrue();
        assertThat(productRepository.findAll().size()).isEqualTo(9);
    }


}
