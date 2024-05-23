package com.greengrim.green.core.alarm;

import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.core.alarm.dto.AlarmResponseDto.AlarmInfo;
import com.greengrim.green.core.member.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
                .interactedMemberId(memberId)
                .build();
        alarmRepository.save(alarm);
        return alarm;
    }

    public PageResponseDto<List<AlarmInfo>> getAlarms(Member member, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Alarm> alarms = alarmRepository.findByMemberWithinAMonth(member, LocalDateTime.now().minusMonths(1), pageable);

        List<AlarmInfo> alarmInfoList = new ArrayList<>();
        alarms.forEach(alarm ->
                alarmInfoList.add(new AlarmInfo(alarm)));

        // 안 읽은 알람 모두 읽음 처리
        alarms.forEach(Alarm::setChecked);

        return new PageResponseDto<>(alarms.getNumber(), alarms.hasNext(), alarmInfoList);
    }
}
