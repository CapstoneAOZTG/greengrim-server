package com.greengrim.green.core.issue.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class IssueResponseDto {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IssueInfo {
        private String title;
        private String imgUrl;
        private String url;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HomeIssues {
        private List<IssueInfo> issueInfos;
    }
}
