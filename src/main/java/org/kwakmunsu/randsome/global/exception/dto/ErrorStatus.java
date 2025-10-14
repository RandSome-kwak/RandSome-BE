package org.kwakmunsu.randsome.global.exception.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus {

    // COMMON
    BAD_REQUEST      (400, "ERROR - 잘못된 요청입니다."),
    FORBIDDEN_MODIFY (403, "ERROR - 수정 권한이 없습니다."),
    FORBIDDEN_DELETE (403, "ERROR - 삭제 권한이 없습니다."),
    NOT_FOUND        (404, "ERROR - 찾을 수 없습니다."),
    DUPLICATE        (409, "ERROR - 중복 되었습니다."),

    // MEMBER
    BAD_REQUEST_MEMBER (400, "ERROR - 잘못된 회원 요청"),
    INVALID_PASSWORD   (401, "ERROR - 비밀번호가 일치하지 않습니다."),
    NOT_FOUND_MEMBER   (404, "ERROR - 회원을 찾을 수 없습니다."),
    NOT_FOUND_LOGIN_ID (404, "ERROR - 존재하지 않는 아이디입니다."),
    DUPLICATE_NICKNAME (409, "ERROR - 이미 존재하는 닉네임입니다."),
    DUPLICATE_LOGIN_ID (409, "ERROR - 이미 존재하는 로그인 ID 입니다."),

    // CANDIDATE
    NOT_FOUND_CANDIDATE (404, "ERROR - 후보자를 찾을 수 없습니다."),
    PENDING_MEMBER      (409, "ERROR - 승인 대기중인 회원입니다."),
    ALREADY_APPROVED    (409, "ERROR - 이미 승인된 회원입니다."),
    ALREADY_REJECTED    (409, "ERROR - 이미 거절된 회원입니다."),

    // MATCHING_APPLICATION
    NOT_FOUND_MATCHING_APPLICATION (404, "ERROR - 매칭 신청 정보를 찾을 수 없습니다."),
    ALREADY_MATCHING_APPLICATION   (409, "ERROR - 이미 매칭 신청이 존재합니다."),
    CANNOT_CANCEL_MATCHING         (400, "ERROR - 매칭 취소가 불가능합니다."),

    // MATCHING
    FORBIDDEN_READ_MATCHING       (403, "ERROR - 매칭 조회 권한이 없습니다."),
    FORBIDDEN_READ_MATCHING_OWNER (403, "ERROR - 본인의 매칭 이력이 아닙니다."),
    NOT_FOUND_MATCHING            (404, "ERROR - 매칭 정보를 찾을 수 없습니다."),


    // INQUIRY
    NOT_FOUND_INQUIRY              (404, "ERROR - 문의를 찾을 수 없습니다."),
    ALREADY_ANSWERED               (409, "ERROR - 이미 답변이 등록된 문의입니다."),
    CANNOT_MODIFY_ANSWER           (409, "ERROR - 답변이 완료된 문의에 답변을 수정할 수 없습니다."),
    CANNOT_MODIFY_ANSWERED_INQUIRY (409, "ERROR - 답변이 완료된 문의는 수정할 수 없습니다."),
    CANNOT_DELETE_ANSWERED_INQUIRY (409, "ERROR - 답변이 완료된 문의는 삭제할 수 없습니다."),



    // PAYMENT
    INVALID_PAYMENT   (400, "ERROR - 유효하지 않은 결제 정보입니다."),
    PAYMENT_FAILED    (400, "ERROR - 결제에 실패하였습니다."),
    NOT_FOUND_PAYMENT (404, "ERROR - 결제 정보를 찾을 수 없습니다."),

    // JWT
    INVALID_TOKEN   (401, "ERROR - 유효하지 않은 토큰입니다."),
    NOT_FOUND_TOKEN (404, "ERROR - 토큰을 찾을 수 없습니다."),

    // AWS
    INVALID_FILE_EXTENSION (400, "ERROR - 지원하지 않는 파일 확장자입니다."),
    NOT_FOUND_FILE         (404, "ERROR - 파일이 존재하지 않습니다."),
    AWS_S3_ERROR           (500, "ERROR - AWS S3 내부 에러"),
    FAILED_TO_UPLOAD_FILE  (500, "ERROR - 파일 업로드에 실패하였습니다."),

    // ETC
    BAD_REQUEST_ARGUMENT   (400, "ERROR - 유효하지 않은 인자입니다."),
    UNAUTHORIZED_ERROR     (401, "ERROR - 인증되지 않은 사용자입니다."),
    EMPTY_SECURITY_CONTEXT (401, "Security Context 에 인증 정보가 없습니다."),
    FORBIDDEN_ERROR        (403, "ERROR - 접근 권한이 없습니다."),
    INTERNAL_SERVER_ERROR  (500, "ERROR - 서버 내부 에러"),
    ;

    private final int statusCode;
    private final String message;

}