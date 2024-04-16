package com.greengrim.green.common.util;

import static com.greengrim.green.common.entity.SortOption.ASC;
import static com.greengrim.green.common.entity.SortOption.DESC;
import static com.greengrim.green.common.entity.SortOption.GREATEST;
import static com.greengrim.green.common.entity.SortOption.LEAST;

import com.greengrim.green.common.entity.SortOption;
import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.GlobalErrorCode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class UtilService {

    public static Pageable getPageable(int page, int size, SortOption sort) {
        if (sort == DESC) { // 최신순
            return PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        } else if (sort == ASC) { // 오래된순
            return PageRequest.of(page, size, Sort.Direction.ASC, "createdAt");
        } else if (sort == GREATEST) {
            return PageRequest.of(page, size, Sort.Direction.DESC, "headCount");
        } else if (sort == LEAST) {
            return PageRequest.of(page, size, Direction.ASC, "headCount");
        } else {
            throw new BaseException(GlobalErrorCode.NOT_VALID_ARGUMENT_ERROR);
        }
    }

    public static boolean checkIsMine(Long memberId, Long ownerId) {
        return Objects.equals(memberId, ownerId);
    }

    public static String formatLocalDateTimeToString(LocalDateTime localDateTime) {
        // LocalDateTime 객체를 포맷팅하여 문자열로 변환
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm"));
    }

}
