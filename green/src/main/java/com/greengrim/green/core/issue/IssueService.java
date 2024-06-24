package com.greengrim.green.core.issue;


import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.IssueErrorCode;
import com.greengrim.green.common.fcm.FcmService;
import com.greengrim.green.core.alarm.entity.AlarmType;
import com.greengrim.green.core.alarm.service.RegisterAlarmService;
import com.greengrim.green.core.issue.dto.IssueRequestDto.IssueRequest;
import com.greengrim.green.core.issue.dto.IssueResponseDto.IssueDetailInfo;
import com.greengrim.green.core.issue.dto.IssueResponseDto.IssueListInfo;
import com.greengrim.green.core.issue.dto.IssueResponseDto.HomeIssues;
import com.greengrim.green.core.issue.dto.IssueResponseDto.IssueInfo;
import java.util.ArrayList;
import java.util.List;

import com.greengrim.green.core.issue.photo.IssuePhoto;
import com.greengrim.green.core.issue.photo.IssuePhotoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final IssuePhotoRepository issuePhotoRepository;
    private final FcmService fcmService;
    private final RegisterAlarmService registerAlarmService;

    private static final int NUMBER_OF_HOME_ISSUES = 5;

    @Transactional
    public void register(IssueRequest issueRequest) {
        Issue issue = Issue.builder()
                        .title(issueRequest.getTitle())
                        .content(issueRequest.getContent())
                        .iconImgUrl(issueRequest.getIconImgUrl())
                        .build();
        issueRepository.save(issue);
        log.info("Issue 저장 성공!");
        issueRequest.getImgUrls().forEach(imgUrl -> {
            log.info("imgUrl : {}", imgUrl.getImgUrl());
                    issuePhotoRepository.save(
                            IssuePhoto.builder()
                                    .issueId(issue.getId())
                                    .imgUrl(imgUrl.getImgUrl())
                                    .build()
                    );
                }
        );
        log.info("Issue 사진 저장 성공!");

        String thumbnail = getThumbnail(issue.getId());

        fcmService.sendNewIssue(issue.getId(), issue.getTitle());
        registerAlarmService.register(null, AlarmType.NEW_ISSUE, issue.getId(), thumbnail, issue.getTitle(), null);
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
            String thumbnail = getThumbnail(issue.getId());
            issueInfos.add( new IssueListInfo(issue.getId(), issue.getTitle(), thumbnail));
        });

        return new PageResponseDto<>(issues.getNumber(), issues.hasNext(), issueInfos);
    }

    public IssueDetailInfo getDetailIssue(Long id) {
        Issue issue = issueRepository.findIssueById(id).
                orElseThrow(() -> new BaseException(IssueErrorCode.EMPTY_ISSUE));
        // 여기서부터는 사진
        List<String> imgUrls = new ArrayList<>();
        List<IssuePhoto> issuePhotos = issuePhotoRepository.findByIssueId(id);
        issuePhotos.forEach(
                issuePhoto -> imgUrls.add(issuePhoto.getImgUrl())
        );
        return new IssueDetailInfo(issue, imgUrls);
    }

    private String getThumbnail(Long id) {
        return issuePhotoRepository.findByThumbnailByIssueId(id);
    }

}
