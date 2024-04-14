package com.greengrim.green.core.hiding.certification;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificationHidingRepository extends JpaRepository<CertificationHiding, Long> {

    Optional<CertificationHiding> findByMemberIdAndCertificationId(Long memberId, Long certificationId);
}
