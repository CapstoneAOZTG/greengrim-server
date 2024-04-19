package com.greengrim.green.core.event;

import com.greengrim.green.core.event.dto.EventResponseDto.EventInfo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    /**
     * [GET] 홈 화면 이벤트 조회
     */
    @Operation(summary = "홈 화면 이벤트 조회")
    @PostMapping("/events")
    public ResponseEntity<EventInfo> getHomeEvent() {
        return ResponseEntity.ok(eventService.getRecentEvent());
    }
}
