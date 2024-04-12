package com.greengrim.green.core.hiding.challenge;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeHidingRepository extends JpaRepository<ChallengeHiding, Long> {

    Optional<ChallengeHiding> findByMemberIdAndChallengeId(Long memberId, Long challengeId);
}
