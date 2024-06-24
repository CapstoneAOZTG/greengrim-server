package com.greengrim.green.core.event.service;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.EventErrorCode;
import com.greengrim.green.core.event.entity.Event;
import com.greengrim.green.core.event.dto.EventResponseDto.EventInfo;
import com.greengrim.green.core.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetEventService {

    private final EventRepository eventRepository;

    public EventInfo getRecentEvent() {
        Event event = eventRepository.findFirstEventByOrderByCreatedAtDesc()
                .orElseThrow(() -> new BaseException(EventErrorCode.EMPTY_EVENT));
        return new EventInfo(event);
    }
}
