package com.example.eBook.domain.post.entity;

import com.example.eBook.domain.base.BaseTimeEntity;
import com.example.eBook.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String subject;

    @Lob
    private String content;
    private String contentHtml;

    public void updateMember(Member member) {
        this.member = member;
    }

    public void updateContentHtml(String contentHtml) {
        this.contentHtml = contentHtml;
    }

    public void updateSubjectAndContent(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }
}
