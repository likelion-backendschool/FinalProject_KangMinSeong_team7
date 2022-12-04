package com.example.ebook.global.mapper;

import com.example.ebook.domain.member.dto.InfoModifyForm;
import com.example.ebook.domain.member.dto.SignupForm;
import com.example.ebook.domain.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {
    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mapping(target="authLevel", constant = "3L")
    @Mapping(target = "restCash", constant = "0")
    @Mapping(target="id", ignore = true)
    Member signupFormToEntity(SignupForm signupForm);

    InfoModifyForm entityToInfoModifyForm(Member member);
}
