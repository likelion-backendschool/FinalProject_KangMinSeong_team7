package com.example.eBook.domain.mapping.postHashTag.repository;

import com.example.eBook.domain.mapping.postHashTag.dto.PostKeywordDto;
import com.example.eBook.domain.mapping.postHashTag.entity.QPostHashTag;
import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.post.dto.PostDto;
import com.example.eBook.domain.post.entity.Post;
import com.example.eBook.global.mapper.PostKeywordMapper;
import com.example.eBook.global.mapper.PostMapper;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
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

    @Override
    public List<PostDto> findPostsByMemberAndKeyword(Member member, List<Long> postKeywordIds) {
        QPostHashTag pht = QPostHashTag.postHashTag;

        return PostMapper.INSTANCE.entitiesToPostDtos(query.select(pht.post)
                        .distinct()
                .where(pht.member.eq(member), inPostKeywordIds(postKeywordIds))
                .from(pht)
                .fetch());
    }

    private BooleanExpression inPostKeywordIds(List<Long> postKeywordIds) {
        if (postKeywordIds == null) {
            return null;
        }
        return QPostHashTag.postHashTag.postKeyword.id.in(postKeywordIds);
    }
}
