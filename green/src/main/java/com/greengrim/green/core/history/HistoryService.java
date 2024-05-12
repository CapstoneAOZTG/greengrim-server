package com.greengrim.green.core.history;


import static com.greengrim.green.common.entity.Time.calculateTime;

import com.greengrim.green.common.entity.dto.PageResponseDto;
import com.greengrim.green.core.history.dto.HistoryResponseDto.HistoryInfo;
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
public class HistoryService {

    private final HistoryRepository historyRepository;

    @Transactional
    public void save(Long memberId, Long targetId, String title, String imgUrl,
                     HistoryOption historyOption, int point, int totalPoint) {
        History history = History.builder()
                .memberId(memberId).targetId(targetId).title(title).imgUrl(imgUrl)
                .historyOption(historyOption).point(point).totalPoint(totalPoint)
                .build();
        historyRepository.save(history);
    }

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
