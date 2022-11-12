package com.example.eBook.domain.withdraw.repository;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.withdraw.entity.QWithdrawApply;
import com.example.eBook.domain.withdraw.entity.WithdrawApply;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class WithdrawRepositoryImpl implements CustomWithdrawRepository {

    private final JPAQueryFactory query;

    public WithdrawRepositoryImpl(EntityManager entityManager) {
        this.query = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<WithdrawApply> findRecentOneByApplicant(Member applicant) {
        QWithdrawApply wd = QWithdrawApply.withdrawApply;

        return query.selectFrom(wd)
                .where(wd.applicant.eq(applicant))
                .orderBy(wd.applyDate.desc())
                .limit(1)
                .fetch();
    }
}
