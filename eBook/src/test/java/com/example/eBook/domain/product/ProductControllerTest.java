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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    @DisplayName("상품목록조회")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void showProductList() throws Exception {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        PostKeyword postKeyword = postKeywordRepository.save(new PostKeyword(1L, "#keyword1"));

        List<Product> productList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            productList.add(Product.builder()
                    .member(member)
                    .postKeyword(postKeyword)
                    .subject("subject %s".formatted(i))
                    .description("description %s".formatted(i))
                    .price(i * 1000)
                    .build());
        }
        productRepository.saveAll(productList);

        // when then
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

        // when then
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

        // given
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        // when then
        mockMvc.perform(post("/product/create")
                        .param("subject", "new subject")
                        .param("price", "10000")
                        .param("description", "new description"))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("create"))
                .andExpect(redirectedUrl("/product/list"));

        assertThat(productRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품상세정보_보여주기")
    @WithMockUser(username = "test_username", password = "1234", roles = "USER")
    void showProductDetail() throws Exception {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        PostKeyword postKeyword = postKeywordRepository.save(new PostKeyword(1L, "#keyword1"));
        Product product = productRepository.save(new Product(1L, member, postKeyword, "subject", "description", 1000));

        // when then
        ResultActions resultActions = mockMvc.perform(get("/product/%s".formatted(product.getId())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("product/detail_product"))
                .andExpect(model().attributeExists("productDetailDto"))
                .andDo(print());

        ProductDetailDto result = (ProductDetailDto) resultActions.andReturn().getModelAndView().getModel().get("productDetailDto");

        assertAll(
                () -> assertThat(result.getSubject()).isEqualTo("subject"),
                () -> assertThat(result.getDescription()).isEqualTo("description"),
                () -> assertThat(result.getPrice()).isEqualTo(1000),
                () -> assertThat(result.getWriter()).isEqualTo("test_username"),
                () -> assertThat(result.getPostKeywordContent()).isEqualTo(postKeyword.getContent())
        );
    }

    @Test
    @DisplayName("상품수정폼_보여주기")
    @WithMockUser(username = "test_username", password = "1234", roles = "WRITER")
    void showModifyForm() throws Exception {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        PostKeyword postKeyword = postKeywordRepository.save(new PostKeyword(1L, "#keyword1"));
        Product product = productRepository.save(new Product(1L, member, postKeyword, "subject", "description", 1000));

        // when then
        ResultActions resultActions = mockMvc.perform(get("/product/%s/modify".formatted(product.getId())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("product/modify_product"))
                .andExpect(model().attributeExists("productModifyForm"))
                .andDo(print());

        ProductModifyForm result = (ProductModifyForm) resultActions.andReturn().getModelAndView().getModel().get("productModifyForm");

        assertThat(result.getSubject()).isEqualTo("subject");
        assertThat(result.getPrice()).isEqualTo(1000);
        assertThat(result.getDescription()).isEqualTo("description");
    }

    @Test
    @DisplayName("상품수정")
    @WithMockUser(username = "test_username", password = "1234", roles = "WRITER")
    void modify() throws Exception {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        PostKeyword postKeyword = postKeywordRepository.save(new PostKeyword(1L, "#keyword1"));
        Product product = productRepository.save(new Product(1L, member, postKeyword, "subject", "description", 1000));

        // when then
        mockMvc.perform(post("/product/%s/modify".formatted(product.getId()))
                        .param("subject", "modify subject")
                        .param("description", "modify description")
                        .param("price", "2000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(redirectedUrlPattern("/product/**"));

        Product findProduct = productRepository.findById(product.getId()).get();

        assertThat(findProduct.getSubject()).isEqualTo("modify subject");
        assertThat(findProduct.getDescription()).isEqualTo("modify description");
        assertThat(findProduct.getPrice()).isEqualTo(2000);
    }

    @Test
    @DisplayName("상품삭제")
    @WithMockUser(username = "test_username", password = "1234", roles = "WRITER")
    void delete() throws Exception {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        PostKeyword postKeyword = postKeywordRepository.save(new PostKeyword(1L, "#keyword1"));
        Product product = productRepository.save(new Product((long) 1L, member, postKeyword, "subject", "description", 1000));

        // when then
        mockMvc.perform(get("/product/%s/delete".formatted(product.getId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(ProductController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(redirectedUrl("/product/list"));

        assertThat(productRepository.findById(product.getId()).isEmpty()).isTrue();
        assertThat(productRepository.findAll().size()).isEqualTo(0);
    }


}
