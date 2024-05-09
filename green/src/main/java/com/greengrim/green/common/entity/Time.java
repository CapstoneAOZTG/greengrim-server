package com.greengrim.green.common.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Time {

    public static String calculateTime(LocalDateTime dateTime, int displayTimeWay) {
        LocalDateTime currentTime = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(dateTime, currentTime);
        long hours = ChronoUnit.HOURS.between(dateTime, currentTime);
        long days = ChronoUnit.DAYS.between(dateTime, currentTime);

        if (displayTimeWay == 1) { //날짜만 표시
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } else if (displayTimeWay == 2) { //날짜와 시간 표시
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } else if (displayTimeWay == 3) { //실시간 반영 시간 표시
            if (minutes == 0) {
                return "방금";
            } else if (minutes < 60) {
                return minutes + "분 전";
            } else if (hours < 24) {
                return hours + "시간 전";
            } else {
                return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
        } else if (displayTimeWay == 4) { //실시간 반영 시간 및 날짜 표시
            if (minutes == 0) {
                return "방금";
            } else if (minutes < 60) {
                return minutes + "분 전";
            } else if (hours < 24) {
                return hours + "시간 전";
            } else {
                return days + "일 전";
            }
        } else { // 알림 시간 표시
            if (hours < 24) { // 오늘이면
                return dateTime.format(DateTimeFormatter.ofPattern("a hh:mm"));
            } else { // 오늘이 아니면
                return dateTime.format(DateTimeFormatter.ofPattern("MM월 dd일"));
            }
        }
    }
}
