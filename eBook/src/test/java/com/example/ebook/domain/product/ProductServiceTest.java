package com.example.ebook.domain.product;

import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.member.repository.MemberRepository;
import com.example.ebook.domain.postkeyword.entity.PostKeyword;
import com.example.ebook.domain.postkeyword.repository.PostKeywordRepository;
import com.example.ebook.domain.product.dto.ProductCreateForm;
import com.example.ebook.domain.product.dto.ProductDetailDto;
import com.example.ebook.domain.product.dto.ProductModifyForm;
import com.example.ebook.domain.product.entity.Product;
import com.example.ebook.domain.product.repository.ProductRepository;
import com.example.ebook.domain.product.service.ProductService;
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
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Slf4j
@Transactional
@ActiveProfiles("test")
public class ProductServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PostKeywordRepository postKeywordRepository;

    @Test
    @DisplayName("상품목록조회")
    void findAll() {

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

        // when
        int size = productService.findAll().size();

        // then
        assertThat(size).isEqualTo(10);
    }

    @Test
    @DisplayName("상품저장")
    void save() {

        // given
        memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));
        postKeywordRepository.save(new PostKeyword(1L, "#keyword1"));
        PostKeyword postKeyword = postKeywordRepository.save(new PostKeyword(2L, "#keyword2"));

        ProductCreateForm productCreateForm = ProductCreateForm.builder()
                .subject("new subject")
                .price(1000)
                .description("new description")
                .postKeywordList(postKeywordRepository.findAll())
                .postKeyword(postKeyword)
                .build();

        // when
        Product testProduct = productService.save("test_username", productCreateForm);

        // then
        assertAll(
                () -> assertThat(productRepository.findById(testProduct.getId()).isPresent()).isTrue(),
                () -> assertThat(productRepository.findAll().size()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("상품상세정보_보여주기")
    void getProductDetail() {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        PostKeyword postKeyword = postKeywordRepository.save(new PostKeyword(1L, "#keyword1"));
        Product product = productRepository.save(new Product(1L, member, postKeyword, "subject", "description", 1000));

        // when
        ProductDetailDto productDetail = productService.getProductDetail(product.getId());

        // then
        assertAll(
                () -> assertThat(productDetail.getSubject()).isEqualTo("subject"),
                () -> assertThat(productDetail.getPrice()).isEqualTo(1000),
                () -> assertThat(productDetail.getDescription()).isEqualTo("description"),
                () -> assertThat(productDetail.getPostKeywordContent()).isEqualTo("#keyword1")
        );
    }

    @Test
    @DisplayName("상품수정폼_보여주기")
    void getProductModifyForm() {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        PostKeyword postKeyword = postKeywordRepository.save(new PostKeyword(1L, "#keyword1"));
        Product product = productRepository.save(new Product(1L, member, postKeyword, "subject", "description", 1000));

        // when
        ProductModifyForm productModifyForm = productService.getProductModifyForm(product.getId());

        // then
        assertAll(
                () -> assertThat(productModifyForm.getSubject()).isEqualTo("subject"),
                () -> assertThat(productModifyForm.getPrice()).isEqualTo(1000),
                () -> assertThat(productModifyForm.getDescription()).isEqualTo("description")
        );
    }

    @Test
    @DisplayName("상품수정")
    void modify() {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        PostKeyword postKeyword = postKeywordRepository.save(new PostKeyword(1L, "#keyword1"));
        Product product = productRepository.save(new Product(1L, member, postKeyword, "subject", "description", 1000));

        ProductModifyForm productModifyForm = ProductModifyForm.builder()
                .subject("modify subject")
                .price(2000)
                .description("modify description")
                .build();

        // when
        productService.modify(product.getId(), productModifyForm);

        // then
        Product testProduct = productRepository.findById(product.getId()).orElseThrow();

        assertAll(
                () -> assertThat(testProduct.getSubject()).isEqualTo(productModifyForm.getSubject()),
                () -> assertThat(testProduct.getPrice()).isEqualTo(productModifyForm.getPrice()),
                () -> assertThat(testProduct.getDescription()).isEqualTo(productModifyForm.getDescription())
        );
    }

    @Test
    @DisplayName("상품삭제")
    void delete() {

        // given
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now(), 0));

        PostKeyword postKeyword = postKeywordRepository.save(new PostKeyword(1L, "#keyword1"));
        Product product = productRepository.save(new Product(1L, member, postKeyword, "subject", "description", 1000));

        // when
        productService.delete(product.getId());

        // then
        assertThat(productRepository.findAll().size()).isEqualTo(0);
    }
}
