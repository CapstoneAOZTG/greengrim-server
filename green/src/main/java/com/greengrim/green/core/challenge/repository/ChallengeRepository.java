package com.greengrim.green.core.challenge.repository;

import com.greengrim.green.core.challenge.Category;
import com.greengrim.green.core.challenge.Challenge;
import com.greengrim.green.core.member.Member;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    @Query(value = "SELECT c FROM Challenge c WHERE c.category=:category AND c.status=true")
    Page<Challenge> findByCategoryAndStateIsTrue(@Param("category") Category category, Pageable pageable);

    /**
     * 내가 만든 챌린지 조회 - 최신순, 오래된 순, 인원 많은 순, 인원 적은 순
     */
    @Query(value = "SELECT c FROM Challenge c WHERE c.member=:member AND c.status=true")
    Page<Challenge> findByMemberAndStateIsTrue(@Param("member") Member member, Pageable pageable);

    /**
     * 핫 챌린지 조회 - 참여 인원이 가장 많은
     */
    @Query(value = "SELECT c FROM Challenge c WHERE c.status=true ORDER BY c.headCount DESC")
    Page<Challenge> findHotChallengesByHeadCount(Pageable pageable);

    Challenge findByChatroomId(Long chatroomId);

    /**
     * Category 입력되지 않으면 전체 챌린지 조회, 입력되면 해당 Category 의 챌린지만 조회
     */
    @Query("SELECT c FROM Challenge c WHERE( (LOWER(c.title) LIKE LOWER(concat('%', :keyword, '%')))"
            + "OR (LOWER(c.description) LIKE LOWER(concat('%', :keyword, '%')))) "
            + "AND c.status = true AND (:category IS NULL OR c.category <> :category) "
            + "ORDER BY c.createdAt DESC")
    Page<Challenge> searchChallenges(@Param("keyword") String keyword, @Param("category") Category category, Pageable pageable);

    Page<Challenge> findAllAndStatusIsTrue(Pageable pageable);

    @Query("SELECT c FROM Challenge c WHERE c IN (SELECT cc.challenge FROM Certification cc WHERE cc.createdAt >= :weekAgo GROUP BY cc.challenge ORDER BY COUNT(cc) DESC)")
    Page<Challenge> findMostCertifiedChallengesWithinAWeek(@Param("weekAgo")LocalDateTime weekAgo, Pageable pageable);

}
