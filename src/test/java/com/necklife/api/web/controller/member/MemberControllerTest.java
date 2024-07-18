// package com.necklife.api.web.controller.member;
//
// import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.Mockito.when;
// import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
// import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
// import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
// import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
// import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
// import com.epages.restdocs.apispec.ResourceSnippetParameters;
// import com.epages.restdocs.apispec.Schema;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.necklife.api.ApiApplication;
// import com.necklife.api.security.token.AuthToken;
// import com.necklife.api.security.token.TokenGenerator;
// import com.necklife.api.security.token.TokenResolver;
// import com.necklife.api.web.controller.description.Description;
// import com.necklife.api.web.dto.request.member.PostOauthMemberBody;
// import com.necklife.api.web.dto.request.member.RefreshMemberAuthTokenBody;
// import com.necklife.api.web.usecase.*;
// import com.necklife.api.web.usecase.dto.response.*;
// import java.time.LocalDateTime;
// import java.util.Optional;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.restdocs.payload.FieldDescriptor;
// import org.springframework.restdocs.payload.JsonFieldType;
// import org.springframework.security.test.context.support.WithUserDetails;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.web.servlet.MockMvc;
//
// @ActiveProfiles(value = "local")
// @AutoConfigureRestDocs
// @AutoConfigureMockMvc(addFilters = false)
// @SpringBootTest(classes = ApiApplication.class)
// class MemberControllerTest {
//
//	@Autowired private MockMvc mockMvc;
//	@Autowired private ObjectMapper objectMapper;
//	private static final String TAG = "MemberControllerTest";
//	private static final String BASE_URL = "/api/v1/members";
//
//	@MockBean TokenGenerator tokenGenerator;
//	@MockBean TokenResolver tokenResolver;
//
//	@MockBean PostMemberUseCase postMemberUseCase;
//	@MockBean DeleteMemberUseCase deleteMemberUseCase;
//	@MockBean GetMemberDetailUseCase getMemberDetailUseCase;
//	@MockBean GetMemberTokenDetailUseCase getMemberTokenDetailUseCase;
//
//	@Test
//	@DisplayName("POST /api/v1/members 회원 가입을 한다.")
//	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
//	void postMember() throws Exception {
//		when(postMemberUseCase.execute(any(), any()))
//				.thenReturn(new PostMemberUseCaseResponse(1L, "이메일", "제공자", "상태"));
//		when(tokenGenerator.generateAuthToken(any(), any()))
//				.thenReturn(new AuthToken("accessToken", "refreshToken"));
//
//		PostOauthMemberBody postMemberBody =
//				PostOauthMemberBody.builder().code("dsakfjdsakfj").provider("구글").build();
//
//		String content = objectMapper.writeValueAsString(postMemberBody);
//
//		mockMvc
//				.perform(
//						post(BASE_URL)
//								.contentType(MediaType.APPLICATION_JSON)
//								.content(content)
//								.header("Authorization", "Bearer {{ accessToken }}"))
//				.andExpect(status().is2xxSuccessful())
//				.andDo(
//						document(
//								"postMember",
//								resource(
//										ResourceSnippetParameters.builder()
//												.description("회원 가입을 한다.")
//												.tag(TAG)
//												.requestSchema(Schema.schema("PostMemberRequest"))
//												.requestHeaders(Description.authHeader())
//												.responseSchema(Schema.schema("PostMemberResponse"))
//												.responseFields(
//														Description.common(
//																new FieldDescriptor[] {
//																	fieldWithPath("data")
//																			.type(JsonFieldType.OBJECT)
//																			.description("데이터"),
//																	fieldWithPath("data.id")
//																			.type(JsonFieldType.NUMBER)
//																			.description("회원 ID"),
//																	fieldWithPath("data.email")
//																			.type(JsonFieldType.STRING)
//																			.description("이메일"),
//																	fieldWithPath("data.provider")
//																			.type(JsonFieldType.STRING)
//																			.description("제공자"),
//																	fieldWithPath("data.status")
//																			.type(JsonFieldType.STRING)
//																			.description("상태"),
//																	fieldWithPath("data.accessToken")
//																			.type(JsonFieldType.STRING)
//																			.description("액세스 토큰"),
//																	fieldWithPath("data.refreshToken")
//																			.type(JsonFieldType.STRING)
//																			.description("리프레시 토큰")
//																}))
//												.build())));
//	}
//
//	@Test
//	@DisplayName("DELETE /api/v1/members 회원 탈퇴를 한다.")
//	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
//	void deleteMember() throws Exception {
//		when(deleteMemberUseCase.execute(anyLong()))
//				.thenReturn(new DeleteMemberUseCaseResponse(1L, LocalDateTime.now()));
//
//		mockMvc
//				.perform(
//						delete(BASE_URL)
//								.contentType(MediaType.APPLICATION_JSON)
//								.header("Authorization", "Bearer {{ accessToken }}"))
//				.andExpect(status().is2xxSuccessful())
//				.andDo(
//						document(
//								"deleteMember",
//								resource(
//										ResourceSnippetParameters.builder()
//												.description("회원 탈퇴를 한다.")
//												.tag(TAG)
//												.requestSchema(Schema.schema("DeleteMemberRequest"))
//												.requestHeaders(Description.authHeader())
//												.responseSchema(Schema.schema("DeleteMemberResponse"))
//												.responseFields(
//														Description.common(
//																new FieldDescriptor[] {
//																	fieldWithPath("data")
//																			.type(JsonFieldType.OBJECT)
//																			.description("데이터"),
//																	fieldWithPath("data.id")
//																			.type(JsonFieldType.NUMBER)
//																			.description("회원 ID"),
//																	fieldWithPath("data.deletedAt")
//																			.type(JsonFieldType.ARRAY)
//																			.description("삭제 시간")
//																}))
//												.build())));
//	}
//
//	@Test
//	@DisplayName("GET /api/v1/members 회원 정보를 조회한다.")
//	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
//	void getMember() throws Exception {
//		when(getMemberDetailUseCase.execute(anyLong()))
//				.thenReturn(new GetMemberDetailUseCaseResponse(1L, "nickname", "profile", "unpaid"));
//
//		mockMvc
//				.perform(
//						get(BASE_URL)
//								.contentType(MediaType.APPLICATION_JSON)
//								.header("Authorization", "Bearer {{ accessToken }}"))
//				.andExpect(status().is2xxSuccessful())
//				.andDo(
//						document(
//								"getMember",
//								resource(
//										ResourceSnippetParameters.builder()
//												.description("회원 정보를 조회한다.")
//												.tag(TAG)
//												.requestSchema(Schema.schema("GetMemberRequest"))
//												.requestHeaders(Description.authHeader())
//												.responseSchema(Schema.schema("GetMemberResponse"))
//												.responseFields(
//														Description.common(
//																new FieldDescriptor[] {
//																	fieldWithPath("data")
//																			.type(JsonFieldType.OBJECT)
//																			.description("데이터"),
//																	fieldWithPath("data.id")
//																			.type(JsonFieldType.NUMBER)
//																			.description("id"),
//																	fieldWithPath("data.email")
//																			.type(JsonFieldType.STRING)
//																			.description("회원 이메일"),
//																	fieldWithPath("data.provider")
//																			.type(JsonFieldType.STRING)
//																			.description("제공자"),
//																	fieldWithPath("data.status")
//																			.type(JsonFieldType.STRING)
//																			.description("상태")
//																}))
//												.build())));
//	}
//
//	@Test
//	@DisplayName("POST /api/v1/members/token 회원 토큰을 갱신한다.")
//	@WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
//	void getMemberToken() throws Exception {
//		when(tokenResolver.resolveId(any())).thenReturn(Optional.of(1L));
//
//		when(getMemberTokenDetailUseCase.execute(anyLong()))
//				.thenReturn(new GetMemberTokenDetailUseCaseResponse(1L));
//
//		when(tokenGenerator.generateAuthToken(any(), any()))
//				.thenReturn(new AuthToken("accessToken", "refreshToken"));
//
//		RefreshMemberAuthTokenBody refreshToken =
//				RefreshMemberAuthTokenBody.builder().refreshToken("refresh").build();
//
//		String content = objectMapper.writeValueAsString(refreshToken);
//
//		mockMvc
//				.perform(post(BASE_URL + "/token").contentType(MediaType.APPLICATION_JSON).content(content))
//				.andExpect(status().is2xxSuccessful())
//				.andDo(
//						document(
//								"refreshMemberAuthToken",
//								resource(
//										ResourceSnippetParameters.builder()
//												.description("회원 토큰을 갱신한다.")
//												.tag(TAG)
//												.requestSchema(Schema.schema("RefreshMemberAuthTokenRequest"))
//												.responseSchema(Schema.schema("RefreshMemberAuthTokenResponse"))
//												.responseFields(
//														Description.common(
//																new FieldDescriptor[] {
//																	fieldWithPath("data")
//																			.type(JsonFieldType.OBJECT)
//																			.description("데이터"),
//																	fieldWithPath("data.accessToken")
//																			.type(JsonFieldType.STRING)
//																			.description("액세스 토큰"),
//																	fieldWithPath("data.refreshToken")
//																			.type(JsonFieldType.STRING)
//																			.description("리프레시 토큰")
//																}))
//												.build())));
//	}
//
//	//    @Test
//	//    @DisplayName("PATCH /api/v1/members/profile 프로필 이미지를 수정한다.")
//	//    @WithUserDetails(userDetailsServiceBeanName = "testTokenUserDetailsService")
//	//    void patchProfile() throws Exception {
//	//        when(patchProfileImageUseCase.execute(anyLong(), any()))
//	//                .thenReturn(new PatchProfileImageUseCaseResponse(1L, "nickname", "profile"));
//	//
//	//        File file = makeFile("src/test/resources/images", "test", "png");
//	//        PatchProfileBody patchProfileBody =
//	//                PatchProfileBody.builder()
//	//                        .profile(
//	//                                new MockMultipartFile(
//	//                                        "profile",
//	//                                        file.getName(),
//	//                                        "image/png",
//	//                                        new BufferedInputStream(new FileInputStream(file))))
//	//                        .build();
//	//
//	//        StringBuilder stringBuilder = new StringBuilder();
//	//        stringBuilder.append("{\n");
//	//        stringBuilder.append("  \"profile\": \"");
//	//        stringBuilder.append(Arrays.toString(patchProfileBody.getProfile().getBytes()));
//	//        stringBuilder.append("\"\n");
//	//        stringBuilder.append("}");
//	//        String content = stringBuilder.toString();
//	//
//	//        mockMvc
//	//                .perform(
//	//                        multipart(BASE_URL + "/profile")
//	//                                .file((MockMultipartFile) patchProfileBody.getProfile())
//	//                                .header("Authorization", "Bearer {{ accessToken }}")
//	//                                .contentType(MediaType.MULTIPART_FORM_DATA)
//	//                                .content(content)
//	//                                .with(
//	//                                        request -> {
//	//                                            request.setMethod("PATCH");
//	//                                            return request;
//	//                                        }))
//	//                .andExpect(status().is2xxSuccessful())
//	//                .andDo(
//	//                        document(
//	//                                "patchProfileImage",
//	//                                resource(
//	//                                        ResourceSnippetParameters.builder()
//	//                                                .description("프로필 이미지를 수정한다.")
//	//                                                .tag(TAG)
//	//
//	// .requestSchema(Schema.schema("PatchProfileImageRequest"))
//	//                                                .requestHeaders(Description.authHeader())
//	//
//	// .responseSchema(Schema.schema("PatchProfileImageResponse"))
//	//                                                .requestFields(
//	//                                                        fieldWithPath("profile")
//	//                                                                .type(JsonFieldType.STRING)
//	//                                                                .description("프로필 이미지"))
//	//                                                .responseFields(
//	//                                                        Description.common(
//	//                                                                new FieldDescriptor[] {
//	//                                                                        fieldWithPath("data")
//	//
//	// .type(JsonFieldType.OBJECT)
//	//
//	// .description("데이터"),
//	//
//	// fieldWithPath("data.id")
//	//	//
//	//	// .type(JsonFieldType.NUMBER)
//	//	//
//	// .description("회원
//	// ID"),
//	//
//	// fieldWithPath("data.nickname")
//	//
//	// .type(JsonFieldType.STRING)
//	//
//	// .description("닉네임"),
//	//
//	// fieldWithPath("data.profile")
//	//
//	// .type(JsonFieldType.STRING)
//	//
//	// .description("프로필 이미지")
//	//                                                                }))
//	//                                                .build())));
//	//    }
//	//
//	//    File makeFile(String path, String pictureName, String pictureExtension) {
//	//        String picture = combineDot(pictureName, pictureExtension);
//	//        File directory = new File(path);
//	//        if (!directory.exists()) {
//	//            directory.mkdirs();
//	//        }
//	//        String testPicturePath = combinePath(path, picture);
//	//        return new File(testPicturePath);
//	//    }
//	//
//	//    String combinePath(String s1, String s2) {
//	//        return s1 + "/" + s2;
//	//    }
//	//
//	//    String combineDot(String s1, String s2) {
//	//        return s1 + "." + s2;
//	//    }
// }
