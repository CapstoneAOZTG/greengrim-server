package com.greengrim.green.core.report.repository;

import com.greengrim.green.core.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
