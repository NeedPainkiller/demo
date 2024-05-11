package xyz.needpainkiller.demo.lib.error;

import org.springframework.http.HttpStatus;

/**
 * 공통 에러 코드
 */
public enum CommonErrorCode implements ErrorCode {
    LOGIN_REQUEST_MISSING(HttpStatus.BAD_REQUEST, "아이디 또는 패스워드를 확인 할 수 없습니다."),
    LOGIN_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "잘못된 패스워드 입니다."),
    LOGIN_NEED_OAUTH_PROVIDER(HttpStatus.BAD_REQUEST, "해당 계정은 소셜 로그인으로 만 로그인이 가능합니다"),
    LOGIN_FAILED_UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 이유로 인해 로그인에 실패 하였습니다.\n 관리자에게 문의 해 주시길 바랍니다."),

    TOKEN_MUST_REQUIRED(HttpStatus.UNAUTHORIZED, "Token 데이터 누락 (비어있음)"),
    TOKEN_HEADER_MUST_REQUIRED(HttpStatus.UNAUTHORIZED, "Token Header 정보 누락 (헤더 검색 실패)"),
    TOKEN_COOKIE_MUST_REQUIRED(HttpStatus.UNAUTHORIZED, "Token Cookie 정보 누락 (쿠키 검색 실패)"),
    TOKEN_MEMORY_INVALID(HttpStatus.UNAUTHORIZED, "로그인 이력이 존해하지 않는 Token"),
    TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, "빈 Token 는 사용 불가능"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Token 만료됨"),
    TOKEN_VALIDATION_FAILED(HttpStatus.UNAUTHORIZED, "Token 이 만료되었거나 손상되어 인증에 실패함"),
    TOKEN_FAILED_CREATE(HttpStatus.INTERNAL_SERVER_ERROR, "유저정보는 확인 성공, Token 발급 실패"),
    TOKEN_CLAIM_PARSE_FAILED(HttpStatus.BAD_REQUEST, "Token 정보가 손상되어 확인 할 수 없음"),
    TOKEN_CLAIM_USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "Token 의 계정 정보를 확인할 수 없음"),
    TOKEN_CLAIM_AUTHORITY_NOT_EXIST(HttpStatus.BAD_REQUEST, "Token 의 권한 정보를 확인할 수 없음"),

    USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "계정 권한이 확인되지 않음"),
    USER_FORBIDDEN(HttpStatus.FORBIDDEN, "요청하고자 하는 정보의 조회권한 없음"),
    USER_NOT_EXIST(HttpStatus.NOT_FOUND, "존재하지 않는 계정입니다."),
    USER_LOCKED(HttpStatus.BAD_REQUEST, "잠금 처리된 계정입니다."),
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 등록된 유저"),
    USER_DISABLED(HttpStatus.BAD_REQUEST, "비활성화 또는 삭제된 유저"),
    USER_DELETE_SELF(HttpStatus.BAD_REQUEST, "자기 자신의 계정을 삭제할 수 없음"),


    ROLE_NOT_EXIST(HttpStatus.NOT_FOUND, "권한정보 조회 실패"),
    ROLE_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 권한"),
    ROLE_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 등록된 권한 ID"),
    ROLE_NOT_MAPPED(HttpStatus.FORBIDDEN, "해당 유저의 권한이 조회되지 않음"),
    ROLE_CAN_NOT_EDITABLE(HttpStatus.BAD_REQUEST, "해당 권한은 수정되거나 삭제 될 수 없습니다."),

    ROLE_USER_CAN_NOT_MANAGE_USER(HttpStatus.FORBIDDEN, "일반유저는 타 유저 관리 불가."),
    ROLE_CAN_NOT_MANAGE_ADMIN(HttpStatus.FORBIDDEN, "Admin 이 아닌 유저는 Admin 계정 관리 불가"),

    USER_ID_LENGTH(HttpStatus.BAD_REQUEST, "계정 ID 의 길이가 조건에 맞지 않음"),
    USER_ID_NEED_ALPHABET(HttpStatus.BAD_REQUEST, "계정 ID 에 알파벳이 존재하지 않음"),
    USER_ID_NEED_NUM(HttpStatus.BAD_REQUEST, "계정 ID 에 숫자가 포함되지 않음"),
    USER_ID_IGNORE_SPECIAL(HttpStatus.BAD_REQUEST, "계정 ID 에 특수문자는 포함되지 않음"),

    USER_ID_IS_NOT_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "이메일 형식을 따를 것"),
    USER_ID_EMPTY(HttpStatus.BAD_REQUEST, "User ID 누락"),
    USER_NM_EMPTY(HttpStatus.BAD_REQUEST, "User Name 누락"),

    PASSWORD_NO_PERMISSION(HttpStatus.FORBIDDEN, "패스워드 변경 권한 없음"),
    PASSWORD_FAIL_UPDATE(HttpStatus.INTERNAL_SERVER_ERROR, "패스워드 변경 또는 초기화 실패"),
    PASSWORD_SAME_PASSWORD(HttpStatus.BAD_REQUEST, "변경하고자 하는 패스워드가 이전과 동일함"),
    PASSWORD_NOT_MATCH_CONFIRM(HttpStatus.BAD_REQUEST, "패스워드가 검증용 패스워드와 동일하지 않음"),
    PASSWORD_EMPTY(HttpStatus.BAD_REQUEST, "패스워드가 비워져 있음"),
    PASSWORD_LENGTH(HttpStatus.BAD_REQUEST, "패스워드의 길이가 조건에 맞지 않음"),
    PASSWORD_NEED_ALPHABET(HttpStatus.BAD_REQUEST, "패스워드에 알파벳이 존재하지 않음"),
    PASSWORD_NEED_NUM_SPECIAL(HttpStatus.BAD_REQUEST, "패스워드에 특수문자, 숫자가 포함되지 않음"),
    PASSWORD_EXPIRED(HttpStatus.BAD_REQUEST, "패스워드가 만료됨"),

    REGISTRATION_USER_TYPE_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "회원가입 불가능한 유저 타입"),
    VERIFICATION_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "본인 인증요청을 하지 않은 계정임"),
    VERIFICATION_ALREADY_DONE(HttpStatus.BAD_REQUEST, "이미 인증된 유저"),
    VERIFICATION_NOT_MATCH(HttpStatus.BAD_REQUEST, "인증코드가 잘못됨"),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "인증코드가 만료됨"),
    VERIFICATION_NOT_VERIFIED(HttpStatus.FORBIDDEN, "본인인증을 하지 않은 유저"),
    VERIFICATION_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "인증메일 발송 실패"),

    CONTENT_NOT_YOU_OWN(HttpStatus.FORBIDDEN, "해당 컨텐츠의 원 작성자가 아님"),

    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "확인되지 않은 메뉴"),
    API_NOT_FOUND(HttpStatus.NOT_FOUND, "확인되지 않은 API 경로"),
    API_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "접근 권한이 확인되지 않는 API"),
    API_REQUEST_EMPTY(HttpStatus.BAD_REQUEST, "API 접근 권한 요청이 누락됨"),
    RESOURCE_DEPENDENCIES_VALID(HttpStatus.BAD_REQUEST, "요청하고자 하는 리소스를 다른 서비스가 사용중입니다."),
    DATETIME_PARSE_FAILED(HttpStatus.BAD_REQUEST, "일자/시간 Data 파싱 실패"),

    FETCH_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Data 조회 실패"),
    HTTP_REFERER_NOT_MATCH(HttpStatus.BAD_REQUEST, "요청 Referer 가 서버 주소와 일치하지 않음 (위조된 웹사이트 공격)"),
    HTTP_RESULT_PARSE_ERROR(HttpStatus.BAD_REQUEST, "HTTP 응답 파싱 이상"),
    REQUEST_JSON_PARSE_ERROR(HttpStatus.BAD_REQUEST, "요청 JSON 파라미터 이상 확인"),
    REQUEST_REQUIRED_DATA_EMPTY(HttpStatus.BAD_REQUEST, "필수 요청 파라미터에서 공백 확인"),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, " Invalid Input Value"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, " 지원하지 않는 HTTP METHOD"),
    REQUEST_IS_CONFLICT(HttpStatus.CONFLICT, " 해당 요청은 특정 리소스의 상태와 충돌됩니다."),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, " Entity Not Found"),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "확인되지 않는 Data"),

    REFERER_NOT_MATCH_CSRF(HttpStatus.FORBIDDEN, "요청 도메인이 서버와 일치하지 않음 (위조된 웹사이트 공격)"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access is Denied"),
    COMMON_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에러");


    private final HttpStatus httpStatus;
    private final String errorMessage;

    CommonErrorCode(HttpStatus status, String errorMessage) {
        this.httpStatus = status;
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }

    @Override
    public String getCode() {
        return name();
    }

}


