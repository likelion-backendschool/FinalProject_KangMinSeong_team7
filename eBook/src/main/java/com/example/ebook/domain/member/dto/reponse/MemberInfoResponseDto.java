package com.example.ebook.domain.member.dto.reponse;

import com.example.ebook.domain.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MemberInfoResponseDto {

    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private String username;
    private String email;
    private boolean emailVerified;
    private String nickname;

    public static MemberInfoResponseDto toResponseDto(Member member) {
        return MemberInfoResponseDto.builder()
                .id(member.getId())
                .createDate(member.getCreateDate())
                .modifyDate(member.getUpdateDate())
                .username(member.getUsername())
                .email(member.getEmail())
                .emailVerified(true)
                .nickname(member.getNickname())
                .build();
    }
}
