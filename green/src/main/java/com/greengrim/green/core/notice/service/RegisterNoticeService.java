package com.greengrim.green.core.notice.service;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.MemberErrorCode;
import com.greengrim.green.core.member.Member;
import com.greengrim.green.core.member.Role;
import com.greengrim.green.core.notice.Notice;
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

    public NoticeDetailInfo register(RegisterNotice registerNotice) {
        Notice notice = Notice.builder()
            .title(registerNotice.getTitle())
            .content(registerNotice.getContent())
            .build();
        noticeRepository.save(notice);
        return new NoticeDetailInfo(notice);
    }
}
