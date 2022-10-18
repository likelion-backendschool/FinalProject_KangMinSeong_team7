package com.example.eBook.domain.postKeyword.service;

import com.example.eBook.domain.postKeyword.entity.PostKeyword;
import com.example.eBook.domain.postKeyword.exception.PostKeywordNotFoundException;
import com.example.eBook.domain.postKeyword.repository.PostKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostKeywordService {

    private final PostKeywordRepository postKeywordRepository;


    public List<PostKeyword> save(String keywords) {
        String[] keyword = keywords.split(" ");
        List<PostKeyword> postKeywords = new ArrayList<>();
        for (String kw : keyword) {
            if (postKeywordRepository.findByContent(kw).isEmpty()) {
                postKeywordRepository.save(PostKeyword.builder().content(kw).build());
            }
            postKeywords.add(postKeywordRepository.findByContent(kw).orElseThrow(
                    () -> new PostKeywordNotFoundException("해당 해시태그가 존재하지 않습니다.")));
        }
        return postKeywords;
    }
}
