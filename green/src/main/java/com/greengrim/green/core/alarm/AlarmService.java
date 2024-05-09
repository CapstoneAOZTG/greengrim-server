package com.greengrim.green.core.alarm;

import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.core.alarm.dto.AlarmResponseDto.AlarmInfo;
import com.greengrim.green.core.member.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    @Transactional
    public Alarm register(Member member, AlarmType alarmType, Long resourceId,
                          String imgUrl, String variableContent, Long memberId) {
        Alarm alarm = Alarm.builder()
                .member(member)
                .type(alarmType)
                .resourceId(resourceId)
                .imgUrl(imgUrl)
                .variableContent(variableContent)
                .memberId(memberId)
                .build();
        alarmRepository.save(alarm);
        return alarm;
    }

}
