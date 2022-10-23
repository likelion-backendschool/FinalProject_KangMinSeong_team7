package com.example.eBook.domain.mapping.postHashTag.repository;

import com.example.eBook.domain.mapping.postHashTag.dto.PostKeywordDto;
import com.example.eBook.domain.mapping.postHashTag.entity.QPostHashTag;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.post.entity.Post;
import com.example.eBook.global.mapper.PostKeywordMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
public class PostHashTagRepositoryImpl implements CustomPostHashTagRepository {

    private final JPAQueryFactory query;

    public PostHashTagRepositoryImpl(EntityManager entityManager) {
        this.query = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<PostKeywordDto> findPostKeywordsByMember(Member member) {
        QPostHashTag pht = QPostHashTag.postHashTag;

        return PostKeywordMapper.INSTANCE.entitiesToPostKeywordDtos(query.select(pht.postKeyword)
                .where(pht.member.eq(member))
                .from(pht)
                .fetch());
    }

    @Override
    public List<PostKeywordDto> findPostKeywordsByPost(Post post) {
        QPostHashTag pht = QPostHashTag.postHashTag;

        return PostKeywordMapper.INSTANCE.entitiesToPostKeywordDtos(query.select(pht.postKeyword)
                .where(pht.post.eq(post))
                .from(pht)
                .fetch());
    }
}
