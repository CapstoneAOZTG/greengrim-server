package com.greengrim.green.core.notice.repository;

import com.greengrim.green.core.notice.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> findNoticeByOrderByCreatedAtDesc(Pageable pageable);

    Notice findNoticeById(Long id);
}
