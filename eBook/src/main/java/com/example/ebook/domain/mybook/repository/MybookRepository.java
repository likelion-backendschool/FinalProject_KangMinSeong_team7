package com.example.ebook.domain.mybook.repository;

import com.example.ebook.domain.member.entity.Member;
import com.example.ebook.domain.mybook.entity.Mybook;
import com.example.ebook.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MybookRepository extends JpaRepository<Mybook, Long> {

    List<Mybook> findAllByMember(Member member);

    @Transactional
    @Modifying
    @Query("delete from Mybook b where b.member =:member and b.product in :productIds")
    void removeByRefund(Member member, List<Product> productIds);
}
