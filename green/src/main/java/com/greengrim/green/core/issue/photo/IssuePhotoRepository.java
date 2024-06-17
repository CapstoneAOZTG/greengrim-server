package com.greengrim.green.core.issue.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssuePhotoRepository extends JpaRepository<IssuePhoto, Long> {

    @Query(value = "SELECT p.imgUrl FROM IssuePhoto p WHERE p.issueId = :issueId ORDER BY p.id ASC LIMIT 1")
    String findByThumbnailByIssueId(@Param("issueId") Long issueId);
    List<IssuePhoto> findByIssueId(@Param("issueId") Long issueId);
}
