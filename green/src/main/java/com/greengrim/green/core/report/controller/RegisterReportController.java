package com.greengrim.green.core.report.controller;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.report.ReportType;
import com.greengrim.green.core.report.dto.ReportRequestDto.RegisterReport;
import com.greengrim.green.core.report.service.RegisterReportService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterReportController {

    private final RegisterReportService registerReportService;

    /**
     * [POST] 신고하기
     * /visitor/report
     */
    @Operation(summary = "신고하기")
    @PostMapping("/visitor/report")
    public ResponseEntity<Integer> registerReportMember(
            @CurrentMember Member member,
            @RequestParam(value = "type") ReportType type,
            @RequestBody @Valid RegisterReport registerReport) {
        registerReportService.registerReport(member, type, registerReport);
        return new ResponseEntity<>(200, HttpStatus.OK);
    }
}
