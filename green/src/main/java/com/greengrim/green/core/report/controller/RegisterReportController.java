package com.greengrim.green.core.report.controller;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.report.entity.ReportType;
import com.greengrim.green.core.report.dto.ReportRequestDto.RegisterReport;
import com.greengrim.green.core.report.service.RegisterReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor/reports")
@Tag(name = "신고하기")
public class RegisterReportController {

    private final RegisterReportService registerReportService;

    /**
     * [POST] 신고하기
     * /visitor/reports
     */
    @Operation(summary = "신고하기")
    @PostMapping
    public ResponseEntity<Integer> registerReportMember(
            @CurrentMember Member member,
            @RequestParam(value = "type") ReportType type,
            @RequestBody @Valid RegisterReport registerReport) {
        registerReportService.registerReport(member, type, registerReport);
        return new ResponseEntity<>(200, HttpStatus.OK);
    }
}
