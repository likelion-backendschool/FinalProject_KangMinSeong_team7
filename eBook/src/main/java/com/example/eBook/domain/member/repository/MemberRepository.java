package com.example.eBook.domain.member.repository;

import com.example.eBook.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
    Optional<Member> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "update member set last_login_time = NOW() where username = :username", nativeQuery = true)
    void updateLastLoginTime(String username);
}
