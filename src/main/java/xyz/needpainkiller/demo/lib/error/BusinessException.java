package xyz.needpainkiller.demo.lib.error;

import lombok.Getter;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * 비즈니스 로직 예외 처리 클래스
 */
@Getter
public class BusinessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -3643175295514351267L;
    private final ErrorCode errorCode;
    private final Map<String, Object> model;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.model = new HashMap<>();
        this.model.put("errorCode", errorCode.getCode());
        this.model.put("errorCodeMessage", errorCode.getMessage());
        this.model.put("errorCodeStatus", errorCode.getStatus());
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.model = new HashMap<>();
        this.model.put("errorCode", errorCode.getCode());
        this.model.put("errorCodeMessage", errorCode.getMessage());
        this.model.put("errorCodeStatus", errorCode.getStatus());
        this.model.put("message", message);
    }

    public BusinessException(ErrorCode errorCode, Map<String, Object> model) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.model = model;
        this.model.put("errorCode", errorCode.getCode());
        this.model.put("errorCodeMessage", errorCode.getMessage());
        this.model.put("errorCodeStatus", errorCode.getStatus());
    }

    public BusinessException(ErrorCode errorCode, String message, Map<String, Object> model) {
        super(message);
        this.errorCode = errorCode;
        this.model = model;
        this.model.put("errorCode", errorCode.getCode());
        this.model.put("errorCodeMessage", errorCode.getMessage());
        this.model.put("errorCodeStatus", errorCode.getStatus());
        this.model.put("message", message);
    }

}
