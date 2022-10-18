package com.example.eBook.global.mapper;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.post.dto.PostDto;
import com.example.eBook.domain.post.entity.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class PostMapperTest {

    @Test
    @DisplayName("PostList_To_PostDtoList_Mapper")
    public void entitiesToPostDtos() {

        Member member = Member.builder()
                .username("test_username")
                .password("1234")
                .email("test@email.com")
                .nickname("test_nickname")
                .authLevel(3L)
                .build();

        List<Post> posts = new ArrayList<>();
        posts.add(Post.builder()
                .member(member).content("content1").subject("subject1").contentHtml("contentHtml1").build());
        posts.add(Post.builder()
                .member(member).content("content2").subject("subject2").contentHtml("contentHtml2").build());

        List<PostDto> postDtoList = PostMapper.INSTANCE.entitiesToPostDtos(posts);

        Assertions.assertThat(postDtoList.get(0).getWriter()).isEqualTo(posts.get(0).getMember().getNickname());
        Assertions.assertThat(postDtoList.get(0).getSubject()).isEqualTo(posts.get(0).getSubject());
        Assertions.assertThat(postDtoList.get(0).getCreateDate()).isEqualTo(posts.get(0).getCreateDate());
        Assertions.assertThat(postDtoList.get(0).getUpdateDate()).isEqualTo(posts.get(0).getUpdateDate());

        Assertions.assertThat(postDtoList.get(1).getWriter()).isEqualTo(posts.get(1).getMember().getNickname());
    }

}