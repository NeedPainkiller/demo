package xyz.needpainkiller.demo.lib.error;

import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;


/**
 * API 응답 에러 객체
 *
 * @author needpainkiller6512
 */
@Getter
public class ApiErrorResponse {

    /**
     * message : 에러 메시지
     * status : HTTP 상태 코드
     * code : 에러 코드
     * model : 추가 정보
     * cause : 에러 원인
     */
    private final String message;
    private final HttpStatus status;
    private final String code;
    private final Map<String, Object> model;
    private final String cause;


    private ApiErrorResponse(final ErrorCode code) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
        this.model = Collections.emptyMap();
        this.cause = Strings.EMPTY;
    }

    private ApiErrorResponse(final ErrorCode code, Map<String, Object> model) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
        this.model = model;
        this.cause = Strings.EMPTY;
    }

    private ApiErrorResponse(final ErrorCode code, String cause) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
        this.model = Collections.emptyMap();
        this.cause = cause;
    }

    private ApiErrorResponse(final ErrorCode code, Map<String, Object> model, String cause) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
        this.model = model;
        this.cause = cause;
    }


    public static ApiErrorResponse of(final ErrorCode code) {
        return new ApiErrorResponse(code);
    }


    public static ApiErrorResponse of(final ErrorCode code, Map<String, Object> model) {
        return new ApiErrorResponse(code, model);
    }

    public static ApiErrorResponse of(final ErrorCode code, Throwable e) {
        return new ApiErrorResponse(code, e.getMessage());
    }

    public static ApiErrorResponse of(final ErrorCode code, Map<String, Object> model, Throwable e) {
        return new ApiErrorResponse(code, model, e.getMessage());
    }

    public static ApiErrorResponse of(BusinessException e) {
        return new ApiErrorResponse(e.getErrorCode(), e.getMessage());
    }

    public static ApiErrorResponse of(Map<String, Object> model, BusinessException e) {
        return new ApiErrorResponse(e.getErrorCode(), model, e.getMessage());
    }

    @Override
    public String toString() {
        return '{' +
                "\"message\":\"" + message + "\"" +
                ", \"status\":\"" + status + "\"" +
                ", \"code\":\"" + code + "\"" +
                ", \"model\":\"" + model + "\"" +
                ", \"cause\":\"" + cause + "\"" +
                '}';
    }
}
