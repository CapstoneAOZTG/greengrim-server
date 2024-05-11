package com.greengrim.green.core.notice.controller;

import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.notice.dto.NoticeRequestDto.RegisterNotice;
import com.greengrim.green.core.notice.dto.NoticeResponseDto.NoticeDetailInfo;
import com.greengrim.green.core.notice.service.RegisterNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class RegisterNoticeController {

    private final RegisterNoticeService registerNoticeService;

    /**
     * [POST] 공지사항 등록
     */
    @Operation(summary = "공지사항 등록")
    @PostMapping("/notices")
    public ResponseEntity<NoticeDetailInfo> registerNotice(
        @CurrentMember Member member,
        @RequestBody RegisterNotice registerNotice) {
        return ResponseEntity.ok(registerNoticeService.register(member, registerNotice));
    }

}
