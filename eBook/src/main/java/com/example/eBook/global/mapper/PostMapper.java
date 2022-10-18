package com.example.eBook.global.mapper;

import com.example.eBook.domain.post.dto.PostDetailDto;
import com.example.eBook.domain.post.dto.PostDto;
import com.example.eBook.domain.post.dto.PostModifyForm;
import com.example.eBook.domain.post.dto.PostWriteForm;
import com.example.eBook.domain.post.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);
    @Mapping(target = "writer", source = "member.nickname")
    PostDto entityToPostDto(Post post);

    List<PostDto> entitiesToPostDtos(List<Post> postList);

    Post postWriteFormToEntity(PostWriteForm postWriteForm);

    @Mapping(target = "writer", source = "member.nickname")
    PostDetailDto entityToPostDetailDto(Post post);

    PostModifyForm entityToPostModifyForm(Post post);
}
