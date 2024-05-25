package xyz.needpainkiller.demo.api.user.error;


import xyz.needpainkiller.demo.lib.error.BusinessException;
import xyz.needpainkiller.demo.lib.error.ErrorCode;

import java.util.Map;

/**
 * 계정 예외
 *
 * @author needpainkiller6512
 */
public class UserException extends BusinessException {

    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UserException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public UserException(ErrorCode errorCode, Map<String, Object> model) {
        super(errorCode, model);
    }

    public UserException(ErrorCode errorCode, String message, Map<String, Object> model) {
        super(errorCode, message, model);
    }
}
