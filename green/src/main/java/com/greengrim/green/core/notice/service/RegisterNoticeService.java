package com.greengrim.green.core.notice.service;

import com.greengrim.green.common.fcm.FcmService;
import com.greengrim.green.core.notice.entity.Notice;
import com.greengrim.green.core.notice.dto.NoticeRequestDto.RegisterNotice;
import com.greengrim.green.core.notice.dto.NoticeResponseDto.NoticeDetailInfo;
import com.greengrim.green.core.notice.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterNoticeService {

    private final NoticeRepository noticeRepository;
    private final FcmService fcmService;

    public NoticeDetailInfo register(RegisterNotice registerNotice) {
        Notice notice = Notice.builder()
            .title(registerNotice.getTitle())
            .content(registerNotice.getContent())
            .build();
        noticeRepository.save(notice);
        // FCM 전송
        fcmService.sendNewNotice(notice.getId(), notice.getTitle());
        return new NoticeDetailInfo(notice);
    }
}
