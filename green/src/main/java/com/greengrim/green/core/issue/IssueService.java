package com.greengrim.green.core.issue;


import com.greengrim.green.core.alarm.AlarmService;
import com.greengrim.green.core.issue.dto.IssueRequestDto.IssueRequest;
import com.greengrim.green.core.issue.dto.IssueResponseDto.HomeIssues;
import com.greengrim.green.core.issue.dto.IssueResponseDto.IssueInfo;
import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;
    private final AlarmService alarmService;

    private static final int NumberOfHomeIssues = 5;

    @Transactional
    public void register(IssueRequest issueRequest) {
        issueRepository.save(
                Issue.builder()
                        .title(issueRequest.getTitle())
                        .imgUrl(issueRequest.getImgUrl())
                        .url(issueRequest.getUrl())
                        .build()
        );
        //TODO: 알람 기능 추가 alarmService.register();
    }

    public HomeIssues getHomeIssues() {
        Page<Issue> issues = issueRepository.findIssueByOrderByCreatedAtDesc(
                PageRequest.of(0, NumberOfHomeIssues));
        List<IssueInfo> issueInfos = new ArrayList<>();
        issues.forEach(issue -> issueInfos.add(
                new IssueInfo(issue.getTitle(), issue.getImgUrl(), issue.getUrl())));
        return new HomeIssues(issueInfos);
    }

}
