package com.greengrim.green.common.util;

import static com.greengrim.green.common.entity.SortOption.ASC;
import static com.greengrim.green.common.entity.SortOption.DESC;
import static com.greengrim.green.common.entity.SortOption.GREATEST;
import static com.greengrim.green.common.entity.SortOption.LEAST;

import com.greengrim.green.common.entity.NftSortOption;
import com.greengrim.green.common.entity.SortOption;
import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.GlobalErrorCode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class UtilService {

    @Value("${profile-img-url}")
    private static String profileImgUrl;

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

    public static PageRequest getNftPageable(int page, int size, NftSortOption sortOption) {
        if(sortOption == NftSortOption.DESC) {
            return PageRequest.of(page, size, Direction.DESC, "createdAt");
        } else if(sortOption == NftSortOption.ASC) {
            return PageRequest.of(page, size, Direction.ASC, "createdAt");
        } else if(sortOption == NftSortOption.FAVORITE) {
            return PageRequest.of(page, size, Direction.DESC, "likeCount");
        } else if(sortOption == NftSortOption.TOKEN_ID) {
            return PageRequest.of(page, size, Direction.ASC, "tokenId");
        }
        else {
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

    /**
     * imgUrl이 Blank라면 프로필 기본 이미지 반환
     */
    public static String checkProfileImgUrlIsBasic(String imgUrl) {
        if(Objects.equals(imgUrl, "")) {
            return profileImgUrl;
        }
        return imgUrl;
    }

    public static String getProfileImgUrl() {
        return profileImgUrl;
    }
}
