package com.greengrim.green.core.alarm;

import com.greengrim.green.core.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    /**
     * 알림 조회 - 30일 이내
     */
    @Query("SELECT a "
            + "FROM Alarm a "
            + "WHERE a.createdAt >= :monthAgo "
            + "AND a.member = :member")
    Page<Alarm> findByMemberWithinAMonth(
            @Param("member") Member member,
            @Param("monthAgo") LocalDateTime monthAgo,
            Pageable pageable);
}
