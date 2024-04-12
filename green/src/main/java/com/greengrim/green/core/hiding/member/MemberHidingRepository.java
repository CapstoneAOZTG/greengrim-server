package com.greengrim.green.core.hiding.member;

import com.greengrim.green.core.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberHidingRepository extends JpaRepository<MemberHiding, Long> {

    Optional<MemberHiding> findByMemberIdAndHiddenMember(Long memberId, Member hiddenMember);
}
