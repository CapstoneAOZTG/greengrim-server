package com.greengrim.green.core.history;

import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.history.dto.HistoryResponseDto.HistoryInfo;
import com.greengrim.green.core.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    /**
     * [GET] 내 포인트 조회
     */
    @Operation(summary = "내 포인트 조회")
    @PostMapping("/visitor/points")
    public ResponseEntity<PageResponseDto<List<HistoryInfo>>> getMyHistory(
            @CurrentMember Member member,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size) {
        return ResponseEntity.ok(historyService.getMyHistory(member.getId(), page, size));
    }

}
