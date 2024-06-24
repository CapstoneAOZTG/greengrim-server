package com.greengrim.green.core.history.service;

import com.greengrim.green.core.history.entity.History;
import com.greengrim.green.core.history.entity.HistoryOption;
import com.greengrim.green.core.history.repository.HistoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegisterHistoryService {

    private final HistoryRepository historyRepository;

    @Transactional
    public void register(Long memberId, Long targetId, String title, String imgUrl,
                         HistoryOption historyOption, int point, int totalPoint) {
        History history = History.builder()
                .memberId(memberId).targetId(targetId).title(title).imgUrl(imgUrl)
                .historyOption(historyOption).point(point).totalPoint(totalPoint)
                .build();
        historyRepository.save(history);
    }
}
