package com.greengrim.green.core.alarm.service;


import com.greengrim.green.core.alarm.entity.Alarm;
import com.greengrim.green.core.alarm.entity.AlarmType;
import com.greengrim.green.core.alarm.repository.AlarmRepository;
import com.greengrim.green.core.member.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterAlarmService {
    private final AlarmRepository alarmRepository;

    @Transactional
    public Alarm register(Member member, AlarmType alarmType, Long resourceId,
                          String imgUrl, String variableContent, Long interactedMemberId) {
        if (interactedMemberId == null) {
            interactedMemberId = -1L;
        }
        Alarm alarm = Alarm.builder()
                .member(member)
                .type(alarmType)
                .resourceId(resourceId)
                .imgUrl(imgUrl)
                .variableContent(variableContent)
                .interactedMemberId(interactedMemberId)
                .build();
        alarmRepository.save(alarm);
        return alarm;
    }
}