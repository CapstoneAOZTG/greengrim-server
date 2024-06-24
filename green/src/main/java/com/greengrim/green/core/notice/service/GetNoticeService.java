package com.greengrim.green.core.notice.service;

import com.greengrim.green.core.notice.entity.Notice;
import com.greengrim.green.core.notice.dto.NoticeResponseDto.NoticeDetailInfo;
import com.greengrim.green.core.notice.dto.NoticeResponseDto.NoticeSimpleInfo;
import com.greengrim.green.core.notice.repository.NoticeRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetNoticeService {

    private final NoticeRepository noticeRepository;

    private static final int NUMBER_OF_SIMPLE_NOTICES = 10;

    public List<NoticeSimpleInfo> getNoticeSimpleInfos() {
        Page<Notice> notices = noticeRepository.findNoticeByOrderByCreatedAtDesc(
            PageRequest.of(0, NUMBER_OF_SIMPLE_NOTICES));
        List<NoticeSimpleInfo> noticeSimpleInfos = new ArrayList<>();
        notices.forEach(notice -> noticeSimpleInfos.add(
            new NoticeSimpleInfo(notice)));
        return noticeSimpleInfos;
    }

    public NoticeDetailInfo getNoticeDetailInfo(Long id) {
        Notice notice = noticeRepository.findNoticeById(id);
        return new NoticeDetailInfo(notice);
    }

}
