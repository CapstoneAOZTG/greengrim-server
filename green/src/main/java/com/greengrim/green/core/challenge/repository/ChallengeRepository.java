package com.greengrim.green.core.challenge.repository;

import com.greengrim.green.core.challenge.entity.Category;
import com.greengrim.green.core.challenge.entity.Challenge;
import com.greengrim.green.core.member.entity.Member;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    Optional<Challenge> findByIdAndStatusIsTrue(@Param("id") Long id);

    @Query("SELECT c FROM Challenge c WHERE c.member = :member")
    List<Challenge> findByMember(@Param("member") Member member);

    @Query(value = "SELECT c " +
            "FROM Challenge c " +
            "LEFT JOIN MemberHiding mh ON c.member = mh.hiddenMember AND mh.memberId = :memberId " +
            "LEFT JOIN ChallengeHiding ch ON c.id = ch.challengeId AND ch.memberId = :memberId " +
            "WHERE c.category = :category AND c.status = true " +
            "AND mh.hiddenMember IS NULL " +
            "AND ch.challengeId IS NULL")
    Page<Challenge> findByCategoryAndStateIsTrue(
            @Param("memberId") Long memberId, @Param("category") Category category, Pageable pageable);

    /**
     * 내가 만든 챌린지 조회 - 최신순, 오래된 순, 인원 많은 순, 인원 적은 순
     */
    @Query(value = "SELECT c FROM Challenge c WHERE c.member=:member AND c.status=true")
    Page<Challenge> findByMemberAndStateIsTrue(@Param("member") Member member, Pageable pageable);

    /**
     * 멤버 별 참여중인 챌린지 조회 - 최신순, 오래된 순, 인원 많은 순, 인원 적은 순
     */
    @Query(value = "SELECT c "
            + "FROM Challenge c "
            + "JOIN Chatroom cr ON c.chatroom.id = cr.id "
            + "JOIN Chatparticipant cp ON cr.id = cp.chatroom.id "
            + "LEFT JOIN MemberHiding mh ON c.member = mh.hiddenMember AND mh.memberId = :memberId "
            + "LEFT JOIN ChallengeHiding ch ON c.id = ch.challengeId AND ch.memberId = :memberId "
            + "WHERE cp.member.id = :targetId AND c.status = true "
            + "AND mh.hiddenMember IS NULL "
            + "AND ch.challengeId IS NULL ")
    Page<Challenge> findByMemberIdAndStateIsTrue(@Param("memberId") Long memberId,
                                                 @Param("targetId") Long targetId,
                                                 Pageable pageable);
    @Query(value = "SELECT c "
        + "FROM Challenge c "
        + "JOIN Chatroom cr ON c.chatroom.id = cr.id "
        + "JOIN Chatparticipant cp ON cr.id = cp.chatroom.id "
        + "LEFT JOIN MemberHiding mh ON c.member = mh.hiddenMember AND mh.memberId = :memberId "
        + "LEFT JOIN ChallengeHiding ch ON c.id = ch.challengeId AND ch.memberId = :memberId "
        + "WHERE cp.member.id = :targetId AND c.status = true "
        + "AND mh.hiddenMember IS NULL "
        + "AND ch.challengeId IS NULL ")
    List<Challenge> findListByMemberIdAndStateIsTrue(@Param("memberId") Long memberId,
                                                     @Param("targetId") Long targetId);

    /**
     * Category 입력되지 않으면 전체 챌린지 조회, 입력되면 해당 Category 의 챌린지만 조회
     */
    @Query("SELECT c "
            + "FROM Challenge c "
            + "LEFT JOIN MemberHiding mh ON c.member = mh.hiddenMember AND mh.memberId = :memberId "
            + "LEFT JOIN ChallengeHiding ch ON c.id = ch.challengeId AND ch.memberId = :memberId "
            + "WHERE( (LOWER(c.title) LIKE LOWER(concat('%', :keyword, '%')))"
            + "OR (LOWER(c.description) LIKE LOWER(concat('%', :keyword, '%')))) "
            + "AND c.status = true AND (:category IS NULL OR c.category <> :category) "
            + "AND mh.hiddenMember IS NULL "
            + "AND ch.challengeId IS NULL "
            + "ORDER BY c.createdAt DESC")
    Page<Challenge> searchChallenges(@Param("memberId") Long memberId,
                                     @Param("keyword") String keyword,
                                     @Param("category") Category category, Pageable pageable);

    /**
     * 핫 챌린지 조회 - 참여 인원이 가장 많은
     */
    @Query(value = "SELECT c "
            + "FROM Challenge c "
            + "LEFT JOIN MemberHiding mh ON c.member = mh.hiddenMember AND mh.memberId = :memberId "
            + "LEFT JOIN ChallengeHiding ch ON c.id = ch.challengeId AND ch.memberId = :memberId "
            + "WHERE c.status = true "
            + "AND (:exceptionId IS NULL OR c.id <> :exceptionId) "
            + "AND mh.hiddenMember IS NULL "
            + "AND ch.challengeId IS NULL "
            + "ORDER BY c.headCount DESC")
    Page<Challenge> findHotChallengesByHeadCount(@Param("memberId") Long memberId,
                                                 @Param("exceptionId") Long exceptionId,
                                                 Pageable pageable);

    /**
     * 핫 챌린지 조회 - 가장 최근에 생성된
     */
    @Query(value = "SELECT c "
            + "FROM Challenge c "
            + "LEFT JOIN MemberHiding mh ON c.member = mh.hiddenMember AND mh.memberId = :memberId "
            + "LEFT JOIN ChallengeHiding ch ON c.id = ch.challengeId AND ch.memberId = :memberId "
            + "WHERE c.status = true "
            + "AND (:exceptionId1 IS NULL OR c.id <> :exceptionId1) "
            + "AND (:exceptionId2 IS NULL OR c.id <> :exceptionId2) "
            + "AND mh.hiddenMember IS NULL "
            + "AND ch.challengeId IS NULL "
            + "ORDER BY c.createdAt DESC")
    Page<Challenge> findAllAndStatusIsTrueDesc(@Param("memberId") Long memberId,
                                               @Param("exceptionId1") Long exceptionId1,
                                               @Param("exceptionId2") Long exceptionId2,
                                               Pageable pageable);

    /**
     * 핫 챌린지 조회 - 일주일 내의 인증이 가장 많은
     */
    @Query("SELECT c "
            + "FROM Challenge c "
            + "JOIN Certification cert ON c = cert.challenge "
            + "LEFT JOIN MemberHiding mh ON c.member = mh.hiddenMember AND mh.memberId = :memberId "
            + "LEFT JOIN ChallengeHiding ch ON c.id = ch.challengeId AND ch.memberId = :memberId "
            + "WHERE cert.createdAt >= :weekAgo "
            + "AND cert.status = true "
            + "AND c.status = true "
            + "AND mh.hiddenMember IS NULL "
            + "AND ch.challengeId IS NULL "
            + "GROUP BY c "
            + "ORDER BY COUNT(cert) DESC")
    Page<Challenge> findMostCertifiedChallengesWithinAWeek(
            @Param("memberId") Long memberId,
            @Param("weekAgo")LocalDateTime weekAgo,
            Pageable pageable);

}
