package com.greengrim.green.core.issue;

import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.core.issue.dto.IssueRequestDto.IssueRequest;
import com.greengrim.green.core.issue.dto.IssueResponseDto.IssueDetailInfo;
import com.greengrim.green.core.issue.dto.IssueResponseDto.IssueListInfo;
import com.greengrim.green.core.issue.dto.IssueResponseDto.HomeIssues;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    /**
     * [GET] 홈 화면 이슈 조회
     */
    @Operation(summary = "홈 화면 이슈 조회",
        description = "최대 5개까지 내려갑니다!")
    @GetMapping("/issues")
    public ResponseEntity<HomeIssues> getRecentIssues() {
        return ResponseEntity.ok(issueService.getHomeIssues());
    }

    /**
     * [POST] 홈 화면 이슈 등록
     */
    @Operation(summary = "홈 화면 이슈 등록")
    @PostMapping("/manager/issues")
    public ResponseEntity<Integer> registerIssues(
            @RequestBody IssueRequest issueRequest) {
        issueService.register(issueRequest);
        return ResponseEntity.ok(200);
    }

    /**
     * [GET] 이슈 목록 조회
     */
    @Operation(summary = "이슈 목록 조회")
    @GetMapping("/issues")
    public ResponseEntity<PageResponseDto<List<IssueListInfo>>> getIssues(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size) {
        return ResponseEntity.ok(issueService.getIssues(page, size));
    }

    /**
     * [GET] 이슈 상세 조회
     */
    @Operation(summary = "이슈 상세 조회")
    @GetMapping("/issues/{id}")
    public ResponseEntity<IssueDetailInfo> getDetailIssue(
            @PathVariable("id") Long id
    ) {
        return ResponseEntity.ok(issueService.getDetailIssue(id));
    }

}
