package com.greengrim.green.core.issue;


import com.greengrim.green.core.issue.dto.IssueResponseDto.HomeIssues;
import com.greengrim.green.core.issue.dto.IssueResponseDto.IssueInfo;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IssueService {

    private final IssueRepository issueRepository;

    private static final int NUMBER_OF_HOME_ISSUES = 5;

    public HomeIssues getHomeIssues() {
        Page<Issue> issues = issueRepository.findIssueByOrderByCreatedAtDesc(
                PageRequest.of(0, NUMBER_OF_HOME_ISSUES));
        List<IssueInfo> issueInfos = new ArrayList<>();
        issues.forEach(issue -> issueInfos.add(
                new IssueInfo(issue.getTitle(), issue.getImgUrl(), issue.getUrl())));
        return new HomeIssues(issueInfos);
    }
}
