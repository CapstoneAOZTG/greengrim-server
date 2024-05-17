package com.greengrim.green.core.notice.controller;

import com.greengrim.green.core.notice.dto.NoticeResponseDto.NoticeDetailInfo;
import com.greengrim.green.core.notice.dto.NoticeResponseDto.NoticeSimpleInfo;
import com.greengrim.green.core.notice.service.GetNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor/notices")
public class GetNoticeController {

    private final GetNoticeService getNoticeService;

    /**
     * [GET] 최근 공지사항 조회
     */
    @Operation(summary = "공지사항 조회",
        description = "최대 10개까지 내려갑니다!")
    @GetMapping
    public ResponseEntity<List<NoticeSimpleInfo>> getNoticeSimpleInfos() {
        return ResponseEntity.ok(getNoticeService.getNoticeSimpleInfos());
    }

    /**
     * [GET] 공지사항 상세 조회
     */
    @Operation(summary = "공지사항 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<NoticeDetailInfo> getNoticeDetailInfo(
        @PathVariable("id") Long id) {
        return ResponseEntity.ok(getNoticeService.getNoticeDetailInfo(id));
    }

}
