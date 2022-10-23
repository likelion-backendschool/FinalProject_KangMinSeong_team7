package com.example.eBook.domain.mapping.postHashTag.repository;

import com.example.eBook.domain.mapping.postHashTag.dto.PostKeywordDto;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.post.dto.PostDto;
import com.example.eBook.domain.post.entity.Post;

import java.util.List;

public interface CustomPostHashTagRepository {
    List<PostKeywordDto> findPostKeywordsByMember(Member member);

    List<PostKeywordDto> findPostKeywordsByPost(Post post);

    List<PostDto> findPostsByMemberAndKeyword(Member member, List<Long> postKeywordIds);
}
