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
        private Long id;
        private String title;
        private String iconImgUrl;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HomeIssues {
        private List<IssueInfo> issueInfos;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IssueDetailInfo {
        private String title;
        private String content;
        private String createdAt;
        private List<String> imgUrl;
    }
}
