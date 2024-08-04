// package com.necklife.api.service;
//
// import com.necklife.api.entity.history.HistoryEntity;
// import com.necklife.api.entity.history.PoseStatus;
// import com.necklife.api.entity.member.MemberEntity;
// import com.necklife.api.entity.member.MemberStatus;
// import com.necklife.api.entity.member.OauthProvider;
// import com.necklife.api.repository.history.HistoryRepository;
// import com.necklife.api.repository.member.MemberRepository;
// import com.necklife.api.web.usecase.dto.request.history.GetYearHistoryRequest;
// import com.necklife.api.web.usecase.dto.response.history.GetYearDetailResponse;
// import com.necklife.api.web.usecase.history.GetYearDetailUseCase;
// import org.junit.Test;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.runner.RunWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.mockito.junit.MockitoJUnitRunner;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.transaction.annotation.Transactional;
//
// import java.util.Arrays;
// import java.util.Calendar;
// import java.util.Date;
// import java.util.List;
//
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.when;
//
// @SpringBootTest
// @ActiveProfiles("local")
// @Transactional
// @RunWith(MockitoJUnitRunner.class)
// public class HistoryServiceTest {
//    @Mock
//    private HistoryRepository historyRepository;
//
//    @InjectMocks
//    private GetYearDetailUseCase getYearDetailUseCase;
//
//
//    @Mock
//    private MemberRepository memberRepository;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testGetYearHistoryData() {
//        // Given
//        String year = "2024";
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(2024, Calendar.JANUARY, 1, 0, 0, 0);
//        Date startDate = calendar.getTime();
//        calendar.set(Calendar.DECEMBER, 31);
//        Date endDate = calendar.getTime();
//
//        MemberEntity testMember = MemberEntity.builder()
//                .email("123")
//                .oauthProvider(OauthProvider.KAKAO)
//                .status(MemberStatus.UNPAID)
//                .build();
//
//        em.persist(testMember);
//        em.flush();
//
//        Long memberId = testMember.getId();
//
//        // Example history data for July
//        HistoryEntity julyHistory = createHistoryEntity(testMember, "2024-07-01T00:00:00Z",
// "2024-07-25T23:59:59Z",
//                createSubHistoryEntity("2024-07-01T10:00:00Z", PoseStatus.FORWARD),
//                createSubHistoryEntity("2024-07-01T12:00:00Z", PoseStatus.BACKWARD),
//                createSubHistoryEntity("2024-07-01T14:00:00Z", PoseStatus.TILTED)
//        );
//
//        // Example history data for other months
//        HistoryEntity januaryHistory = createHistoryEntity(testMember, "2024-01-01T08:00:00Z",
// "2024-01-01T10:00:00Z",
//                createSubHistoryEntity("2024-01-01T08:30:00Z", PoseStatus.FORWARD),
//                createSubHistoryEntity("2024-01-01T09:00:00Z", PoseStatus.BACKWARD)
//        );
//
//        HistoryEntity februaryHistory = createHistoryEntity(testMember, "2024-02-01T08:00:00Z",
// "2024-02-01T10:00:00Z",
//                createSubHistoryEntity("2024-02-01T08:30:00Z", PoseStatus.FORWARD),
//                createSubHistoryEntity("2024-02-01T09:00:00Z", PoseStatus.BACKWARD)
//        );
//
//        List<HistoryEntity> historyEntities = Arrays.asList(januaryHistory, februaryHistory,
// julyHistory);
//
//        // Mock repository call
//        when(historyRepository.findAllByMemberIdAndStartAtBetween(any(), any(),
// any())).thenReturn(historyEntities);
//
//
//
//        // When
//        GetYearHistoryRequest getYearHistoryRequest = new GetYearHistoryRequest(1L,
// Integer.parseInt(year));
//        GetYearDetailResponse response = getYearDetailUseCase.execute(getYearHistoryRequest);
//
//        // Then
//        assertEquals(year, response.getYear());
//        assertEquals(3, response.getMonths().size());
//
//        GetYearDetailResponse.Month january = response.getMonths().get(0);
//        assertEquals("2024-01", january.getMonth());
//        assertEquals(7200, january.getMeasurementTime());
//        assertEquals(1800, january.getForwardTime());
//        assertEquals(1800, january.getBackwardTime());
//        assertEquals(0, january.getTiltedTime());
//        assertEquals(1, january.getForwardCount());
//        assertEquals(1, january.getBackwardCount());
//        assertEquals(0, january.getTiltedCount());
//        assertEquals(3600, january.getNormalTime());
//
//        GetYearDetailResponse.Month february = response.getMonths().get(1);
//        assertEquals("2024-02", february.getMonth());
//        assertEquals(7200, february.getMeasurementTime());
//        assertEquals(1800, february.getForwardTime());
//        assertEquals(1800, february.getBackwardTime());
//        assertEquals(0, february.getTiltedTime());
//        assertEquals(1, february.getForwardCount());
//        assertEquals(1, february.getBackwardCount());
//        assertEquals(0, february.getTiltedCount());
//        assertEquals(3600, february.getNormalTime());
//
//        GetYearDetailResponse.Month july = response.getMonths().get(2);
//        assertEquals("2024-07", july.getMonth());
//        assertEquals(2165999, july.getMeasurementTime());  // Adjust this based on actual test
// data duration
//        assertEquals(7200, july.getForwardTime());
//        assertEquals(7200, july.getBackwardTime());
//        assertEquals(3600, july.getTiltedTime());
//        assertEquals(1, july.getForwardCount());
//        assertEquals(1, july.getBackwardCount());
//        assertEquals(1, july.getTiltedCount());
//        assertEquals(2164799, july.getNormalTime());  // Adjust this based on actual test data
// duration
//    }
//
//    private HistoryEntity createHistoryEntity(MemberEntity member, String startAt, String endAt,
// SummaryHistoryEntity... subHistories) {
//        return HistoryEntity.builder()
//                .member(member)
//                .startAt(Date.from(java.time.Instant.parse(startAt)))
//                .endAt(Date.from(java.time.Instant.parse(endAt)))
//                .subHistory(Arrays.asList(subHistories))
//                .build();
//    }
//
//    private SummaryHistoryEntity createSubHistoryEntity(String changedAt, PoseStatus status) {
//        return SummaryHistoryEntity.builder()
//                .changedAt(Date.from(java.time.Instant.parse(changedAt)))
//                .poseStatus(status)
//                .build();
//    }
// }
