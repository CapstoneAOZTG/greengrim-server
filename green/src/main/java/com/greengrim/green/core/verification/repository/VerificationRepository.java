package com.greengrim.green.core.verification.repository;

import com.greengrim.green.core.verification.entity.Verification;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {

    Optional<Verification> findByMemberIdAndCertificationId(
            @Param("memberId") Long memberId,
            @Param("certificationId") Long certificationId);

    // 올바르지 않다 대답 개수
    Integer countByCertificationIdAndResponse(
            @Param("certificationId") Long certificationId,
            @Param("response") boolean response);

}
