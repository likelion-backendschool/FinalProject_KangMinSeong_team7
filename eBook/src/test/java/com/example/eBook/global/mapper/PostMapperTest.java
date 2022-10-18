package com.example.eBook.global.mapper;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.post.dto.PostDto;
import com.example.eBook.domain.post.dto.PostWriteForm;
import com.example.eBook.domain.post.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

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

        assertThat(postDtoList.get(0).getWriter()).isEqualTo(posts.get(0).getMember().getNickname());
        assertThat(postDtoList.get(0).getSubject()).isEqualTo(posts.get(0).getSubject());
        assertThat(postDtoList.get(0).getCreateDate()).isEqualTo(posts.get(0).getCreateDate());
        assertThat(postDtoList.get(0).getUpdateDate()).isEqualTo(posts.get(0).getUpdateDate());

        assertThat(postDtoList.get(1).getWriter()).isEqualTo(posts.get(1).getMember().getNickname());
    }

    @Test
    @DisplayName("postWriteForm_To_Entity_test")
    public void postWriteFormToEntity() {

        PostWriteForm postWriteForm = PostWriteForm.builder()
                .subject("subject1").content("content1").keywords("#자바 #스프링").build();

        Post post = PostMapper.INSTANCE.postWriteFormToEntity(postWriteForm);

        assertThat(post.getMember()).isNull();
        assertThat(post.getContentHtml()).isNull();
        assertThat(post.getContent()).isEqualTo(postWriteForm.getContent());
        assertThat(post.getSubject()).isEqualTo(postWriteForm.getSubject());

    }
}