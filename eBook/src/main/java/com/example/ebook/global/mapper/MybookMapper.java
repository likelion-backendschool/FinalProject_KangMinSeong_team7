package com.example.ebook.global.mapper;

import com.example.ebook.domain.mybook.dto.MybookDto;
import com.example.ebook.domain.mybook.entity.Mybook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MybookMapper {

    MybookMapper INSTANCE = Mappers.getMapper(MybookMapper.class);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productSubject", source = "product.subject")
    @Mapping(target = "productDescription", source = "product.description")
    @Mapping(target = "productWriter", source = "product.member.nickname")
    @Mapping(target = "productKeywordContent", source = "product.postKeyword.content")
    MybookDto entityToMybookDto(Mybook mybook);


    List<MybookDto> entitiesToMybookDtos(List<Mybook> mybooks);
}
