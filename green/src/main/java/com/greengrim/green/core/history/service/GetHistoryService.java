package com.greengrim.green.core.history.service;


import static com.greengrim.green.common.entity.Time.calculateTime;

import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.core.history.entity.History;
import com.greengrim.green.core.history.entity.HistoryOption;
import com.greengrim.green.core.history.dto.HistoryResponseDto.HistoryInfo;
import com.greengrim.green.core.history.repository.HistoryRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetHistoryService {

    private final HistoryRepository historyRepository;

    public PageResponseDto<List<HistoryInfo>> getMyHistory(Long id, int page, int size) {
        Page<History> histories = historyRepository.findByMemberId(id, PageRequest.of(page, size, Direction.DESC, "createdAt"));
        return makeHistoryInfoListForm(histories);
    }

    public PageResponseDto<List<HistoryInfo>> makeHistoryInfoListForm(Page<History> histories) {
        List<HistoryInfo> historyInfoList = new ArrayList<>();
        histories.forEach(history ->
                historyInfoList.add(
                        new HistoryInfo(
                                history,
                                calculateTime(history.getCreatedAt(), 2))));
        return new PageResponseDto<>(histories.getNumber(), histories.hasNext(), historyInfoList);
    }
}
