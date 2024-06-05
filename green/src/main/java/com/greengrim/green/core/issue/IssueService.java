package com.greengrim.green.core.issue;


import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.common.fcm.FcmService;
import com.greengrim.green.core.alarm.AlarmService;
import com.greengrim.green.core.alarm.AlarmType;
import com.greengrim.green.core.issue.dto.IssueRequestDto.IssueRequest;
import com.greengrim.green.core.issue.dto.IssueResponseDto.IssueListInfo;
import com.greengrim.green.core.issue.dto.IssueResponseDto.HomeIssues;
import com.greengrim.green.core.issue.dto.IssueResponseDto.IssueInfo;
import java.util.ArrayList;
import java.util.List;

import com.greengrim.green.core.issue.photo.IssuePhotoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final IssuePhotoRepository issuePhotoRepository;
    private final FcmService fcmService;
    private final AlarmService alarmService;

    private static final int NUMBER_OF_HOME_ISSUES = 5;

    @Transactional
    public void register(IssueRequest issueRequest) {
        Issue issue = Issue.builder()
                        .title(issueRequest.getTitle())
                        .content(issueRequest.getContent())
                        .iconImgUrl(issueRequest.getIconImgUrl())
                        .build();
        issueRepository.save(issue);

        //TODO: 이슈 사진 리스트 저장 테이블, 로직 추가

        String thumbnail = issuePhotoRepository.findByThumbnailByIssueId(issue.getId());

        fcmService.sendNewIssue(issue.getId(), issue.getTitle());
        alarmService.register(null, AlarmType.NEW_ISSUE, issue.getId(), thumbnail, issue.getTitle(), null);
    }

    public HomeIssues getHomeIssues() {
        Page<Issue> issues = issueRepository.findIssueByOrderByCreatedAtDesc(
                PageRequest.of(0, NUMBER_OF_HOME_ISSUES));
        List<IssueInfo> issueInfos = new ArrayList<>();
        issues.forEach(issue -> issueInfos.add(
                new IssueInfo(issue.getId(), issue.getTitle(), issue.getIconImgUrl())));
        return new HomeIssues(issueInfos);
    }

    public PageResponseDto<List<IssueListInfo>> getIssues(int page, int size) {
        Page<Issue> issues = issueRepository.findIssueByOrderByCreatedAtDesc(PageRequest.of(page, size));
        List<IssueListInfo> issueInfos = new ArrayList<>();

        issues.forEach(issue -> {
            String thumbnail = issuePhotoRepository.findByThumbnailByIssueId(issue.getId());
            issueInfos.add( new IssueListInfo(issue.getId(), issue.getTitle(), thumbnail));
        });

        return new PageResponseDto<>(issues.getNumber(), issues.hasNext(), issueInfos);

    }

}
