//package com.necklife.api.service.history;
//
//import com.necklife.api.entity.history.HistoryEntity;
//import com.necklife.api.entity.history.SubHistoryEntity;
//import com.necklife.api.entity.member.MemberEntity;
//import com.necklife.api.repository.history.HistoryRepository;
//import com.necklife.api.repository.history.SubHistoryRepository;
//import com.necklife.api.web.usecase.dto.request.history.PostSubHistoryDto;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Date;
//import java.util.List;
//
//@Service
//@Transactional
//@AllArgsConstructor
//public class SaveHistoryService {
//
//    private final HistoryRepository historyRepository;
//    private final SubHistoryRepository subHistoryRepository;
//
//    public void execute(MemberEntity memberEntity, Date startAt, Date endAt
//            , List<PostSubHistoryDto> subHistories) {
//
//        //history 만들기
//        HistoryEntity newHistory = HistoryEntity.builder()
//                .member(memberEntity)
//                .startAt(startAt)
//                .endAt(endAt)
//                .build();
//
//        // subHistory 리스트 만들기
//        List<SubHistoryEntity> newSubHistory = subHistories.stream().map(subHistoryDto -> {
//            return SubHistoryEntity.builder()
//                    .history(newHistory)
//                    .changedAt(subHistoryDto.getChangedAt())
//                    .poseStatus(subHistoryDto.getPoseStatus())
//                    .build();
//        }).toList();
//
//
//
//        //subHistory 리스트로 만들어서 bulkInsert + historyId 삽입
//        subHistoryRepository.bulkInsert(newHistory.getId(), newSubHistory);
//
//        // history에 저장한 newSubHistory 업데이트
//        newHistory.updateSubHistory(newSubHistory);
//
//        // history 저장
//        historyRepository.save(newHistory);
//
//    }
//
//
//
//}
