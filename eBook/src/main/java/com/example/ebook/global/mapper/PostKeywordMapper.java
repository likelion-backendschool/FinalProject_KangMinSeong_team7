package com.example.ebook.global.mapper;

import com.example.ebook.domain.mapping.posthashtag.dto.PostKeywordDto;
import com.example.ebook.domain.postkeyword.entity.PostKeyword;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostKeywordMapper {
    PostKeywordMapper INSTANCE = Mappers.getMapper(PostKeywordMapper.class);

    PostKeywordDto entityToPostKeywordDto(PostKeyword postKeyword);
    PostKeyword postKeywordDtoToEntity(PostKeywordDto postKeywordDto);

    List<PostKeywordDto> entitiesToPostKeywordDtos(List<PostKeyword> postKeywords);
}
