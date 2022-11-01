package com.example.eBook.domain.mybook.repository;

import com.example.eBook.domain.member.entity.Member;
import com.example.eBook.domain.mybook.entity.Mybook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MybookRepository extends JpaRepository<Mybook, Long> {

    List<Mybook> findAllByMember(Member member);
}
