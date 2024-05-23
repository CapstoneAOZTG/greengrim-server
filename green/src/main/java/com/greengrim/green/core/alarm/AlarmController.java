package com.greengrim.green.core.alarm;

import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.common.oauth.auth.CurrentMember;
import com.greengrim.green.core.alarm.dto.AlarmResponseDto.AlarmInfo;
import com.greengrim.green.core.member.Member;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor/alarms")
public class AlarmController {

    private final AlarmService alarmService;

    /**
     * [GET] 알림 조회
     */
    @Operation(summary = "알림 조회")
    @GetMapping("")
    public ResponseEntity<PageResponseDto<List<AlarmInfo>>> getAlarms(
            @CurrentMember Member member,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size) {
        return ResponseEntity.ok(alarmService.getAlarms(member, page, size));
    }
}
