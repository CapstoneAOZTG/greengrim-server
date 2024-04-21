package com.greengrim.green.core.issue;

import com.greengrim.green.core.issue.dto.IssueResponseDto.HomeIssues;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    /**
     * [GET] 홈 화면 이슈 조회
     */
    @Operation(summary = "홈 화면 이슈 조회",
        description = "최대 5개까지 내려갑니다!")
    @GetMapping("/home/issues")
    public ResponseEntity<HomeIssues> getRecentIssues() {
        return ResponseEntity.ok(issueService.getHomeIssues());
    }
}
