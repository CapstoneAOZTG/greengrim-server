package com.greengrim.green.common.util;

import static com.greengrim.green.common.entity.SortOption.ASC;
import static com.greengrim.green.common.entity.SortOption.DESC;
import static com.greengrim.green.common.entity.SortOption.GREATEST;
import static com.greengrim.green.common.entity.SortOption.LEAST;

import com.greengrim.green.common.entity.NftSortOption;
import com.greengrim.green.common.entity.SortOption;
import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.GlobalErrorCode;
import java.text.DecimalFormat;
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

    public static String formatIntToString(int number) {
        if (number >= 10000) {
            return (number / 1000) + "K";
        } else {
            DecimalFormat formatter = new DecimalFormat("#,###");
            return formatter.format(number);
        }
    }
}
