package com.example.eBook.global.mapper;

import com.example.eBook.domain.mapping.postHashTag.dto.PostKeywordDto;
import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PostKeywordMapperTest {

    @Test
    @DisplayName("엔티티_To_PostKeywordDto_Mapper")
    void entityToPostKeywordDto() {
        PostKeyword postKeyword = new PostKeyword(1L, "#key1");

        PostKeywordDto postKeywordDto = PostKeywordMapper.INSTANCE.entityToPostKeywordDto(postKeyword);
        Assertions.assertThat(postKeywordDto.getId()).isEqualTo(postKeyword.getId());
        Assertions.assertThat(postKeywordDto.getContent()).isEqualTo(postKeyword.getContent());
    }
}
