package com.greengrim.green.core.history.repository;

import com.greengrim.green.core.history.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {

    Page<History> findByMemberId(Long memberId, Pageable pageable);
}
