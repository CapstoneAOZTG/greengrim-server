package com.greengrim.green.core.issue.dto;

import java.util.List;

import com.greengrim.green.core.issue.entity.Issue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.greengrim.green.common.entity.Time.calculateTime;

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
        private List<String> imgUrls;

        public IssueDetailInfo(Issue issue, List<String> imgUrls) {
            this.title = issue.getTitle();
            this.content = issue.getContent();
            this.createdAt = calculateTime(issue.getCreatedAt(), 3);
            this.imgUrls = imgUrls;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IssueListInfo {
        private Long id;
        private String title;
        private String imgUrl;
    }
}
