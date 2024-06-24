package com.greengrim.green.core.certification.repository;

import com.greengrim.green.core.certification.entity.Certification;
import com.greengrim.green.core.challenge.entity.Challenge;
import com.greengrim.green.core.member.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {

    Optional<Certification> findByIdAndStatusIsTrue(@Param("id") Long id);

    @Query("SELECT COUNT(c) FROM Certification c WHERE c.member= :member AND c.challenge= :challenge AND c.status=true")
    int countsByMemberAndChallenge(
            @Param("member") Member member,
            @Param("challenge") Challenge challenge);

    @Query("SELECT c "
            + "FROM Certification c "
            + "WHERE date_format(c.createdAt, '%Y-%m-%d') = :date "
            + "AND c.member= :member "
            + "AND c.challenge= :challenge "
            + "AND c.status = true")
    Optional<Certification> findByDateAndMemberAndChallenge(
            @Param("date") String date,
            @Param("member") Member member,
            @Param("challenge") Challenge challenge);

    @Query("SELECT DISTINCT date_format(c.createdAt, '%Y-%m-%d') FROM Certification c WHERE c.challenge.id = :challengeId ORDER BY c.createdAt")
    List<String> findCertificationsByChallengeMonth(@Param("challengeId") Long challengeId);

    @Query("SELECT DISTINCT date_format(c.createdAt, '%Y-%m-%d') FROM Certification c WHERE c.member.id = :targetId ORDER BY c.createdAt")
    List<String> findCertificationsByMemberMonth(@Param("targetId") Long targetId);

    @Query("SELECT c "
            + "FROM Certification c "
            + "LEFT JOIN MemberHiding mh ON c.member = mh.hiddenMember AND mh.memberId = :memberId "
            + "LEFT JOIN CertificationHiding ch ON c.id = ch.certificationId AND ch.memberId = :memberId "
            + "WHERE date_format(c.createdAt, '%Y-%m-%d') = :date "
            + "AND c.challenge.id = :challengeId "
            + "AND c.status = true "
            + "AND mh.hiddenMember IS NULL "
            + "AND ch.certificationId IS NULL "
            + "ORDER BY c.createdAt")
    Page<Certification> findCertificationsByChallengeDate(@Param("memberId") Long memberId,
                                                          @Param("date") String date,
                                                          @Param("challengeId") Long challengeId,
                                                          Pageable pageable);

    @Query("SELECT c "
            + "FROM Certification c "
            + "LEFT JOIN MemberHiding mh ON c.member = mh.hiddenMember AND mh.memberId = :memberId "
            + "LEFT JOIN CertificationHiding ch ON c.id = ch.certificationId AND ch.memberId = :memberId "
            + "WHERE date_format(c.createdAt, '%Y-%m-%d') = :date "
            + "AND c.member.id = :targetId "
            + "AND c.status = true "
            + "AND mh.hiddenMember IS NULL "
            + "AND ch.certificationId IS NULL "
            + "ORDER BY c.createdAt")
    Page<Certification> findCertificationsByMemberDate(@Param("memberId") Long memberId,
                                                       @Param("date") String date,
                                                       @Param("targetId") Long targetId,
                                                       Pageable pageable);

    @Query("SELECT c.id "
            + "FROM Certification c "
            + "LEFT JOIN MemberHiding mh ON c.member = mh.hiddenMember AND mh.memberId = :memberId "
            + "LEFT JOIN ChallengeHiding ch ON c.challenge.id = ch.challengeId AND ch.memberId = :memberId "
            + "LEFT JOIN CertificationHiding cth ON c.id = cth.certificationId AND cth.memberId = :memberId "
            + "WHERE c.id NOT IN (SELECT v.certificationId FROM Verification v WHERE v.memberId=:memberId AND v.certificationId=c.id) "
            + "AND c.validation = 0 "
            + "AND c.status = true "
            + "AND c.member.id!=:memberId "
            + "AND mh.hiddenMember IS NULL "
            + "AND ch.challengeId IS NULL "
            + "AND cth.certificationId IS NULL "
            + "ORDER BY c.verificationCount limit 1")
    Optional<Long> findCertificationForVerification(@Param("memberId") Long memberId);

    List<Certification> findByMember(@Param("member") Member member);

    List<Certification> findByChallenge(@Param("challenge") Challenge challenge);

}
