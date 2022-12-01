package com.example.ebook.domain.mapping.posthashtag.repository;

import com.example.ebook.domain.mapping.posthashtag.dto.PostKeywordDto;
import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.post.dto.PostDto;
import com.example.ebook.domain.post.entity.Post;

import java.util.List;

public interface CustomPostHashTagRepository {
    List<PostKeywordDto> findPostKeywordsByMember(Member member);

    List<PostKeywordDto> findPostKeywordsByPost(Post post);

    List<PostDto> findPostsByMemberAndKeyword(Member member, List<Long> postKeywordIds);
}
