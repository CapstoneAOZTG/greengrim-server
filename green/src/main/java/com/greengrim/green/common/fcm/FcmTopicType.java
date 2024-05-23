package com.greengrim.green.common.fcm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FcmTopicType {

    TOPIC_ISSUE("ISSUE"),
    TOPIC_NOTICE("NOTICE");

    private final String title;
}
