package com.example.eBook.domain.member.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MemberInfoResponse {

    private MemberDetail member;

    @Getter
    @Setter
    @AllArgsConstructor
    public class MemberDetail {
        private Long id;
        private LocalDateTime createDate;
        private LocalDateTime modifyDate;
        private String username;
        private String email;
        private boolean emailVerified;
        private String nickname;
    }
}
