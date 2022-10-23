package com.example.eBook.domain.postKeyword;

import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import com.example.eBook.domain.postKeyword.repository.PostKeywordRepository;
import com.example.eBook.domain.postKeyword.service.PostKeywordService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
@Transactional
@ActiveProfiles("test")
public class PostKeywordServiceTest {

    @Autowired
    private PostKeywordService postKeywordService;

    @Autowired
    private PostKeywordRepository postKeywordRepository;

    @Test
    @DisplayName("키워드모두조회")
    void findAll() {
        List<PostKeyword> postKeywords = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            postKeywords.add(new PostKeyword());
        }

        postKeywordRepository.saveAll(postKeywords);

        assertThat(postKeywordService.findAll().size()).isEqualTo(10);
    }

    @Test
    @DisplayName("키워드저장")
    void save() {
        postKeywordService.save("#key1 #key2 #key3 #key4");

        assertThat(postKeywordRepository.count()).isEqualTo(4);
    }

    @Test
    @DisplayName("키워드저장_중복저장_X")
    void save_2() {
        List<PostKeyword> postKeywords = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            postKeywords.add(new PostKeyword((long) i, "#key%s".formatted(i)));
        }

        postKeywordRepository.saveAll(postKeywords);
        postKeywordService.save("#key4 #key5 #key6 #key7");

        assertThat(postKeywordRepository.count()).isEqualTo(7);
    }

    @Test
    @DisplayName("키워드저장_공백저장_X")
    void save_3() {
        postKeywordService.save("  key4   #key5  ");
        postKeywordService.save("   ");

        assertThat(postKeywordRepository.count()).isEqualTo(2);
    }
}
