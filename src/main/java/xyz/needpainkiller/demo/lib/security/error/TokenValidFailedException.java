package xyz.needpainkiller.demo.lib.security.error;

import xyz.needpainkiller.demo.lib.error.BusinessException;
import xyz.needpainkiller.demo.lib.error.ErrorCode;

import java.util.Map;

import static xyz.needpainkiller.demo.lib.error.CommonErrorCode.TOKEN_VALIDATION_FAILED;

/**
 * Json Web Token 검증 실패 예외
 */
public class TokenValidFailedException extends BusinessException {
    public TokenValidFailedException() {
        super(TOKEN_VALIDATION_FAILED);
    }

    public TokenValidFailedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenValidFailedException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public TokenValidFailedException(ErrorCode errorCode, Map<String, Object> model) {
        super(errorCode, model);
    }

    public TokenValidFailedException(ErrorCode errorCode, String message, Map<String, Object> model) {
        super(errorCode, message, model);
    }

}
