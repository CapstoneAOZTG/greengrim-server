package com.greengrim.green.core.event;

import com.greengrim.green.core.event.dto.EventResponseDto.EventInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visitor/events")
@Tag(name = "이벤트")
public class EventController {

    private final EventService eventService;

    /**
     * [GET] 홈 화면 이벤트 조회
     */
    @Operation(summary = "홈 화면 이벤트 조회")
    @GetMapping("/home")
    public ResponseEntity<EventInfo> getHomeEvent() {
        return ResponseEntity.ok(eventService.getRecentEvent());
    }
}
