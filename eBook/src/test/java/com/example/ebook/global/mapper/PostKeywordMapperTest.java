package com.example.ebook.global.mapper;

import com.example.ebook.domain.mapping.posthashtag.dto.PostKeywordDto;
import com.example.ebook.domain.postkeyword.entity.PostKeyword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PostKeywordMapperTest {

    @Test
    @DisplayName("엔티티_To_PostKeywordDto_Mapper")
    void entityToPostKeywordDto() {
        PostKeyword postKeyword = new PostKeyword(1L, "#key1");

        PostKeywordDto postKeywordDto = PostKeywordMapper.INSTANCE.entityToPostKeywordDto(postKeyword);
        assertAll(
                () -> assertThat(postKeywordDto.getId()).isEqualTo(postKeyword.getId()),
                () -> assertThat(postKeywordDto.getContent()).isEqualTo(postKeyword.getContent())
        );
    }
}
