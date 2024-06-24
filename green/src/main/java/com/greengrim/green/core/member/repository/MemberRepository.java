package com.greengrim.green.core.member.repository;

import com.greengrim.green.core.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByNickName(String nickName);

    Optional<Member> findByIdAndStatusTrue(@Param("memberId") Long memberId);

    Optional<Member> findByEmail(String email);
}
