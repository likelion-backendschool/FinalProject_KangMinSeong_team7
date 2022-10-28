package com.example.eBook.domain.product;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.member.repository.MemberRepository;
import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import com.example.eBook.domain.postKeyword.repository.PostKeywordRepository;
import com.example.eBook.domain.product.dto.ProductCreateForm;
import com.example.eBook.domain.product.dto.ProductDetailDto;
import com.example.eBook.domain.product.dto.ProductModifyForm;
import com.example.eBook.domain.product.entity.Product;
import com.example.eBook.domain.product.repository.ProductRepository;
import com.example.eBook.domain.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void beforeEach() {
        Member member = memberRepository.save(new Member(1L, "test_username", passwordEncoder.encode("1234"),
                "test_nickname", "test@email.com", 3L, LocalDateTime.now()));

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
    void findAll() {
        assertThat(productService.findAll().size()).isEqualTo(10);
    }

    @Test
    @DisplayName("상품저장")
    void save() {
        postKeywordRepository.save(new PostKeyword(1L, "#keyword1"));
        PostKeyword postKeyword = postKeywordRepository.save(new PostKeyword(2L, "#keyword2"));

        ProductCreateForm productCreateForm = ProductCreateForm.builder()
                .subject("new subject")
                .price(1000)
                .description("new description")
                .postKeywordList(postKeywordRepository.findAll())
                .postKeyword(postKeyword)
                .build();

        Product testProduct = productService.save("test_username", productCreateForm);
        assertThat(productRepository.findById(testProduct.getId()).isPresent()).isTrue();
        assertThat(productRepository.findAll().size()).isEqualTo(11);
    }

    @Test
    @DisplayName("상품상세정보_보여주기")
    void getProductDetail() {
        ProductDetailDto productDetail = productService.getProductDetail(1L);

        assertThat(productDetail.getSubject()).isEqualTo("subject 1");
        assertThat(productDetail.getPrice()).isEqualTo(1000);
        assertThat(productDetail.getDescription()).isEqualTo("description 1");
        assertThat(productDetail.getPostKeywordContent()).isEqualTo("#keyword1");
    }

    @Test
    @DisplayName("상품수정폼_보여주기")
    void getProductModifyForm() {
        ProductModifyForm productModifyForm = productService.getProductModifyForm(1L);

        assertThat(productModifyForm.getSubject()).isEqualTo("subject 1");
        assertThat(productModifyForm.getPrice()).isEqualTo(1000);
        assertThat(productModifyForm.getDescription()).isEqualTo("description 1");
    }

    @Test
    @DisplayName("상품수정")
    void modify() {
        ProductModifyForm productModifyForm = ProductModifyForm.builder()
                .subject("modify subject")
                .price(2000)
                .description("modify description")
                .build();

        productService.modify(1L, productModifyForm);

        Product testProduct = productRepository.findById(1L).orElseThrow();
        assertThat(testProduct.getSubject()).isEqualTo(productModifyForm.getSubject());
        assertThat(testProduct.getPrice()).isEqualTo(productModifyForm.getPrice());
        assertThat(testProduct.getDescription()).isEqualTo(productModifyForm.getDescription());
    }

    @Test
    @DisplayName("상품삭제")
    void delete() {
        productService.delete(1L);

        assertThat(productRepository.findAll().size()).isEqualTo(9);
    }
}
