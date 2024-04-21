package com.greengrim.green.core.event;

import com.greengrim.green.core.event.dto.EventResponseDto.EventInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public EventInfo getRecentEvent() {
        Event event = eventRepository.findFirstEventByOrderByCreatedAtDesc().orElse(null);
        return new EventInfo(event.getTitle(), event.getImgUrl());
    }
}
