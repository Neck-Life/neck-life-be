package com.necklife.api.web.controller.history;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necklife.api.ApiApplication;
import com.necklife.api.web.dto.request.history.PostPostureHistoryBody;
import com.necklife.api.web.dto.request.history.PostureHistoryEntry;
import com.necklife.api.web.usecase.dto.response.history.GetHistoryPointResponse;
import com.necklife.api.web.usecase.dto.response.history.GetMonthDetailResponse;
import com.necklife.api.web.usecase.dto.response.history.GetYearDetailResponse;
import com.necklife.api.web.usecase.dto.response.history.PostureStatus;
import com.necklife.api.web.usecase.history.GetHistoryPointUseCase;
import com.necklife.api.web.usecase.history.GetMonthDetailUseCase;
import com.necklife.api.web.usecase.history.GetYearDetailUseCase;
import com.necklife.api.web.usecase.history.PostHistoryUseCase;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles(value = "local")
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = ApiApplication.class)
class PostureHistoryControllerTest {

	@Autowired private MockMvc mockMvc;

	@Autowired private ObjectMapper objectMapper;

	private static final String TAG = "PostureHistoryControllerTest";
	private static final String BASE_URL = "/api/v1/history";

	@MockBean private PostHistoryUseCase postHistoryUseCase;

	@MockBean private GetYearDetailUseCase getYearDetailUseCase;

	@MockBean private GetMonthDetailUseCase getMonthDetailUseCase;

	@MockBean private GetHistoryPointUseCase getHistoryPointUseCase;

	@Test
	@DisplayName("POST /api/v1/history 자세 기록을 추가한다.")
	@WithMockUser
	void postHistory() throws Exception {
		PostPostureHistoryBody request =
				PostPostureHistoryBody.builder()
						.startTime(LocalDateTime.parse("2024-07-01T00:00:00"))
						.endTime(LocalDateTime.parse("2024-07-25T23:59:59"))
						.history(
								Collections.singletonList(
										PostureHistoryEntry.builder()
												.startTime(LocalDateTime.parse("2024-07-01T10:00:00"))
												.duration(1800)
												.status("forward")
												.build()))
						.build();

		String content = objectMapper.writeValueAsString(request);

		mockMvc
				.perform(
						post(BASE_URL)
								.contentType(MediaType.APPLICATION_JSON)
								.content(content)
								.header("Authorization", "Bearer {{accessToken}}"))
				.andExpect(status().isOk())
				.andDo(
						document(
								"post-history",
								resource(
										ResourceSnippetParameters.builder()
												.description("자세 기록을 추가한다.")
												.tag(TAG)
												.requestSchema(Schema.schema("PostPostureHistoryRequest"))
												.requestFields(
														fieldWithPath("startTime")
																.type(JsonFieldType.STRING)
																.description("The start time of the history"),
														fieldWithPath("endTime")
																.type(JsonFieldType.STRING)
																.description("The end time of the history"),
														fieldWithPath("history[].startTime")
																.type(JsonFieldType.STRING)
																.description("The start time of the status"),
														fieldWithPath("history[].duration")
																.type(JsonFieldType.NUMBER)
																.description("The duration of the status in seconds"),
														fieldWithPath("history[].status")
																.type(JsonFieldType.STRING)
																.description("The status of the posture"))
												.responseSchema(Schema.schema("PostPostureHistoryResponse"))
												.responseFields(
														fieldWithPath("message")
																.type(JsonFieldType.STRING)
																.description("Response message"),
														fieldWithPath("code")
																.type(JsonFieldType.STRING)
																.description("Response code"),
														fieldWithPath("timestamp")
																.type(JsonFieldType.STRING)
																.description("Timestamp of the response"))
												.build())));
	}

	@Test
	@DisplayName("GET /api/v1/history/month 한 달 자세 기록을 조회한다.")
	@WithMockUser
	void getMonthHistory() throws Exception {
		GetMonthDetailResponse response = new GetMonthDetailResponse();
		response.setMonth("2024-07");
		response.setDays(
				Collections.singletonList(
						new GetMonthDetailResponse.Day(
								"2024-07-01",
								7200,
								1800,
								1200,
								900,
								2,
								2,
								1,
								Collections.singletonList(
										new GetMonthDetailResponse.StatusChange(
												"2024-07-01T09:15:00Z", 600, PostureStatus.FORWARD)))));

		when(getMonthDetailUseCase.execute()).thenReturn(response);

		mockMvc
				.perform(get(BASE_URL + "/month"))
				.andExpect(status().isOk())
				.andDo(
						document(
								"get-month-history",
								resource(
										ResourceSnippetParameters.builder()
												.description("한 달 자세 기록을 조회한다.")
												.tag(TAG)
												.responseSchema(Schema.schema("GetMonthHistoryResponse"))
												.responseFields(
														new FieldDescriptor[] {
															fieldWithPath("message")
																	.type(JsonFieldType.STRING)
																	.description("Response message"),
															fieldWithPath("code")
																	.type(JsonFieldType.STRING)
																	.description("Response code"),
															fieldWithPath("timestamp")
																	.type(JsonFieldType.STRING)
																	.description("Timestamp of the response"),
															fieldWithPath("data.month")
																	.type(JsonFieldType.STRING)
																	.description("The month of the history"),
															fieldWithPath("data.days[].date")
																	.type(JsonFieldType.STRING)
																	.description("The date of the history entry"),
															fieldWithPath("data.days[].totalMeasurementTime")
																	.type(JsonFieldType.NUMBER)
																	.description("Total measurement time in seconds"),
															fieldWithPath("data.days[].totalForwardTime")
																	.type(JsonFieldType.NUMBER)
																	.description("Total forward time in seconds"),
															fieldWithPath("data.days[].totalBackwardTime")
																	.type(JsonFieldType.NUMBER)
																	.description("Total backward time in seconds"),
															fieldWithPath("data.days[].totalTiltedTime")
																	.type(JsonFieldType.NUMBER)
																	.description("Total tilted time in seconds"),
															fieldWithPath("data.days[].totalForwardCount")
																	.type(JsonFieldType.NUMBER)
																	.description("Total forward count"),
															fieldWithPath("data.days[].totalBackwardCount")
																	.type(JsonFieldType.NUMBER)
																	.description("Total backward count"),
															fieldWithPath("data.days[].totalTiltedCount")
																	.type(JsonFieldType.NUMBER)
																	.description("Total tilted count"),
															fieldWithPath("data.days[].statusChanges[].time")
																	.type(JsonFieldType.STRING)
																	.description("The start time of the status change"),
															fieldWithPath("data.days[].statusChanges[].duration")
																	.type(JsonFieldType.NUMBER)
																	.description("The duration of the status change in seconds"),
															fieldWithPath("data.days[].statusChanges[].status")
																	.type(JsonFieldType.STRING)
																	.description("The status of the posture")
														})
												.build())));
	}

	@Test
	@DisplayName("GET /api/v1/history/year 연간 자세 기록을 조회한다.")
	@WithMockUser
	void getYearHistory() throws Exception {
		GetYearDetailResponse response = new GetYearDetailResponse();
		response.setYear("2024");
		response.setMonths(
				Collections.singletonList(
						new GetYearDetailResponse.Month("2024-01", 72000, 18000, 10800, 7200, 15, 10, 12)));

		when(getYearDetailUseCase.execute()).thenReturn(response);

		mockMvc
				.perform(get(BASE_URL + "/year"))
				.andExpect(status().isOk())
				.andDo(
						document(
								"get-year-history",
								resource(
										ResourceSnippetParameters.builder()
												.description("연간 자세 기록을 조회한다.")
												.tag(TAG)
												.responseSchema(Schema.schema("GetYearHistoryResponse"))
												.responseFields(
														new FieldDescriptor[] {
															fieldWithPath("message")
																	.type(JsonFieldType.STRING)
																	.description("Response message"),
															fieldWithPath("code")
																	.type(JsonFieldType.STRING)
																	.description("Response code"),
															fieldWithPath("timestamp")
																	.type(JsonFieldType.STRING)
																	.description("Timestamp of the response"),
															fieldWithPath("data.year")
																	.type(JsonFieldType.STRING)
																	.description("The year of the history"),
															fieldWithPath("data.months[].month")
																	.type(JsonFieldType.STRING)
																	.description("The month of the history entry"),
															fieldWithPath("data.months[].totalMeasurementTime")
																	.type(JsonFieldType.NUMBER)
																	.description("Total measurement time in seconds"),
															fieldWithPath("data.months[].totalForwardTime")
																	.type(JsonFieldType.NUMBER)
																	.description("Total forward time in seconds"),
															fieldWithPath("data.months[].totalBackwardTime")
																	.type(JsonFieldType.NUMBER)
																	.description("Total backward time in seconds"),
															fieldWithPath("data.months[].totalTiltedTime")
																	.type(JsonFieldType.NUMBER)
																	.description("Total tilted time in seconds"),
															fieldWithPath("data.months[].totalForwardCount")
																	.type(JsonFieldType.NUMBER)
																	.description("Total forward count"),
															fieldWithPath("data.months[].totalBackwardCount")
																	.type(JsonFieldType.NUMBER)
																	.description("Total backward count"),
															fieldWithPath("data.months[].totalTiltedCount")
																	.type(JsonFieldType.NUMBER)
																	.description("Total tilted count")
														})
												.build())));
	}

	@Test
	@DisplayName("GET /api/v1/history/point 자세 점수를 조회한다.")
	@WithMockUser
	void getHistoryPoint() throws Exception {
		GetHistoryPointResponse response = new GetHistoryPointResponse();
		response.setTotalScore(86);
		response.setThisMonth(
				new GetHistoryPointResponse.MonthData(72000, 18000, 10800, 7200, 5, 5, 5));
		response.setLastMonth(new GetHistoryPointResponse.MonthData(64800, 21600, 7200, 3600, 4, 4, 3));

		when(getHistoryPointUseCase.execute()).thenReturn(response);

		mockMvc
				.perform(get(BASE_URL + "/point"))
				.andExpect(status().isOk())
				.andDo(
						document(
								"get-history-point",
								resource(
										ResourceSnippetParameters.builder()
												.description("자세 점수를 조회한다.")
												.tag(TAG)
												.responseSchema(Schema.schema("GetHistoryPointResponse"))
												.responseFields(
														new FieldDescriptor[] {
															fieldWithPath("message")
																	.type(JsonFieldType.STRING)
																	.description("Response message"),
															fieldWithPath("code")
																	.type(JsonFieldType.STRING)
																	.description("Response code"),
															fieldWithPath("timestamp")
																	.type(JsonFieldType.STRING)
																	.description("Timestamp of the response"),
															fieldWithPath("data.totalScore")
																	.type(JsonFieldType.NUMBER)
																	.description("Total score"),
															fieldWithPath("data.thisMonth.totalMeasurementTime")
																	.type(JsonFieldType.NUMBER)
																	.description("Total measurement time for this month in seconds"),
															fieldWithPath("data.thisMonth.totalForwardTime")
																	.type(JsonFieldType.NUMBER)
																	.description("Total forward time for this month in seconds"),
															fieldWithPath("data.thisMonth.totalBackwardTime")
																	.type(JsonFieldType.NUMBER)
																	.description("Total backward time for this month in seconds"),
															fieldWithPath("data.thisMonth.totalTiltedTime")
																	.type(JsonFieldType.NUMBER)
																	.description("Total tilted time for this month in seconds"),
															fieldWithPath("data.thisMonth.totalForwardCount")
																	.type(JsonFieldType.NUMBER)
																	.description("Total forward count for this month"),
															fieldWithPath("data.thisMonth.totalBackwardCount")
																	.type(JsonFieldType.NUMBER)
																	.description("Total backward count for this month"),
															fieldWithPath("data.thisMonth.totalTiltedCount")
																	.type(JsonFieldType.NUMBER)
																	.description("Total tilted count for this month"),
															fieldWithPath("data.lastMonth.totalMeasurementTime")
																	.type(JsonFieldType.NUMBER)
																	.description("Total measurement time for last month in seconds"),
															fieldWithPath("data.lastMonth.totalForwardTime")
																	.type(JsonFieldType.NUMBER)
																	.description("Total forward time for last month in seconds"),
															fieldWithPath("data.lastMonth.totalBackwardTime")
																	.type(JsonFieldType.NUMBER)
																	.description("Total backward time for last month in seconds"),
															fieldWithPath("data.lastMonth.totalTiltedTime")
																	.type(JsonFieldType.NUMBER)
																	.description("Total tilted time for last month in seconds"),
															fieldWithPath("data.lastMonth.totalForwardCount")
																	.type(JsonFieldType.NUMBER)
																	.description("Total forward count for last month"),
															fieldWithPath("data.lastMonth.totalBackwardCount")
																	.type(JsonFieldType.NUMBER)
																	.description("Total backward count for last month"),
															fieldWithPath("data.lastMonth.totalTiltedCount")
																	.type(JsonFieldType.NUMBER)
																	.description("Total tilted count for last month")
														})
												.build())));
	}
}
