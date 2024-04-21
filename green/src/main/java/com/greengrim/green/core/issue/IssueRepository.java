package com.greengrim.green.core.issue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    Page<Issue> findIssueByOrderByCreatedAtDesc(Pageable pageable);
}
