package org.kwakmunsu.randsome.domain.member.controller;

import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.BAD_REQUEST;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.DUPLICATE;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.DUPLICATE_INSTAGRAM;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.DUPLICATE_LOGIN_ID;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.DUPLICATE_NICKNAME;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.INTERNAL_SERVER_ERROR;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.NOT_FOUND;
import static org.kwakmunsu.randsome.global.exception.dto.ErrorStatus.UNAUTHORIZED_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kwakmunsu.randsome.domain.member.controller.dto.MemberProfileUpdateRequest;
import org.kwakmunsu.randsome.domain.member.controller.dto.MemberRegisterRequest;
import org.kwakmunsu.randsome.domain.member.service.dto.CheckResponse;
import org.kwakmunsu.randsome.domain.member.service.dto.MemberActivityStatsResponse;
import org.kwakmunsu.randsome.domain.member.service.dto.MemberProfileResponse;
import org.kwakmunsu.randsome.global.swagger.ApiExceptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Tag(name = "Member API", description = "회원 관련 API 문서입니다.")
public abstract class MemberDocsController {

    @Operation(
            summary = "회원 가입 - [JWT X] ",
            description = """
                    ### 신규 회원을 등록한다.
                    - 201 Created 상태 코드를 반환한다.
                    - 요청 본문은 검증 규칙에 따라 유효성 검사를 수행한다.
                    """,
            security = {@SecurityRequirement(name = "")}
    )
    @RequestBody(
            description = """
                    회원 가입 요청 본문
                    - 모든 필드는 필수입니다.
                    
                    """,
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MemberRegisterRequest.class)
            )
    )
    @ApiResponse(
            responseCode = "201",
            description = "고객 회원 생성 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Long.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            NOT_FOUND,
            DUPLICATE,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Long> register(MemberRegisterRequest request);

    @Operation(
            summary = "회원 프로필 조회 - [JWT O]",
            description = """
                    ### 로그인한 회원의 프로필 정보를 조회합니다.
                    - 회원의 기본 정보, 프로필 사진, 선호 설정 등을 포함합니다.
                    - 로그인한 본인의 프로필만 조회할 수 있습니다.
                    - 200 OK 상태 코드와 함께 프로필 정보를 반환합니다.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @ApiResponse(
            responseCode = "200",
            description = "회원 프로필 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MemberProfileResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            NOT_FOUND,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<MemberProfileResponse> getProfile(Long memberId);

    @Operation(
            summary = "로그인 아이디 중복 체크 - [JWT X]",
            description = "회원 가입 시 입력한 로그인 아이디가 이미 존재하는지 확인합니다.",
            security = {@SecurityRequirement(name = "")}
    )
    @ApiResponse(
            responseCode = "200",
            description = "로그인 아이디 사용 가능 여부",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CheckResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            DUPLICATE_LOGIN_ID,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<CheckResponse> checkLoginId(
            @Parameter(
                    name = "loginId",
                    description = "중복 확인할 로그인 아이디",
                    in = ParameterIn.QUERY,
                    required = true
            )
            String loginId
    );

    @Operation(
            summary = "닉네임 중복 체크 - [JWT X]",
            description = "회원 가입 시 입력한 닉네임이 이미 존재하는지 확인합니다.",
            security = {@SecurityRequirement(name = "")}
    )
    @ApiResponse(
            responseCode = "200",
            description = "닉네임 사용 가능 여부",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CheckResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            DUPLICATE_NICKNAME,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<CheckResponse> checkNickname(
            @Parameter(
                    name = "nickname",
                    description = "중복 확인할 닉네임",
                    in = ParameterIn.QUERY,
                    required = true
            )
            String nickname
    );

    @Operation(
            summary = "인스타그램 ID 중복 체크 - [JWT X]",
            description = "회원 가입 시 입력한 인스타그램 ID가 이미 존재하는지 확인합니다.",
            security = {@SecurityRequirement(name = "")}
    )
    @ApiResponse(
            responseCode = "200",
            description = "인스타그램 ID 사용 가능 여부",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CheckResponse.class)
            )
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            DUPLICATE_INSTAGRAM,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<CheckResponse> checkInstagramId(
            @Parameter(
                    name = "instagramId",
                    description = "중복 확인할 인스타그램 ID",
                    in = ParameterIn.QUERY,
                    required = true
            )
            String instagramId
    );

    @Operation(
            summary = "회원 프로필 수정 - [JWT O]",
            description = """
                    ### 로그인한 회원의 프로필 정보를 수정합니다.
                    - 닉네임, MBTI, 인스타그램 ID, 자기소개, 이상형 소개 등을 수정할 수 있습니다.
                    - 요청 본문은 검증 규칙에 따라 유효성 검사를 수행합니다.
                    - 200 OK 상태 코드를 반환합니다.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @RequestBody(
            description = """
                    회원 프로필 수정 요청 본문
                    - 모든 필드는 필수입니다.
                    """,
            required = true,
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MemberProfileUpdateRequest.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "회원 프로필 수정 성공"
    )
    @ApiExceptions(values = {
            BAD_REQUEST,
            UNAUTHORIZED_ERROR,
            NOT_FOUND,
            DUPLICATE_NICKNAME,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<Void> updateProfile(Long memberId, MemberProfileUpdateRequest request);


    @Operation(
            summary = "회원 활동 통계 조회 - [JWT O]",
            description = """
                    ### 로그인한 회원의 활동 통계 정보를 조회합니다.
                    - 매칭 신청 횟수, 후보자로 선택된 횟수, 문의 횟수를  포함합니다.
                    - 200 OK 상태 코드와 함께 활동 통계 정보를 반환합니다.
                    """,
            security = {@SecurityRequirement(name = "Bearer ")}
    )
    @ApiResponse(
            responseCode = "200",
            description = "회원 활동 통계 조회 성공",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = MemberActivityStatsResponse.class)
            )
    )
    @ApiExceptions(values = {
            UNAUTHORIZED_ERROR,
            INTERNAL_SERVER_ERROR
    })
    public abstract ResponseEntity<MemberActivityStatsResponse> getMemberActivityInfo(Long memberId);

}