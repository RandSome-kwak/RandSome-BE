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
    FORBIDDEN_PASSWORD (403, "ERROR - 비밀번호가 일치하지 않습니다."),
    NOT_FOUND_MEMBER   (404, "ERROR - 회원을 찾을 수 없습니다."),
    DUPLICATE_NICKNAME (409, "ERROR - 중복되는 닉네임입니다."),
    DUPLICATE_EMAIL    (409, "ERROR - 중복되는 이메일입니다."),
    DUPLICATE_CUSTOMER (409, "ERROR - 이미 존재하는 고객 계정입니다."),
    DUPLICATE_MERCHANT (409, "ERROR - 이미 존재하는 상인 계정입니다."),

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