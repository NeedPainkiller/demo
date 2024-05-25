package xyz.needpainkiller.demo.api.user.error;

import xyz.needpainkiller.demo.lib.error.BusinessException;
import xyz.needpainkiller.demo.lib.error.ErrorCode;

import java.util.Map;

/**
 * 권한 예외
 *
 * @author needpainkiller6512
 */
public class RoleException extends BusinessException {

    public RoleException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RoleException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public RoleException(ErrorCode errorCode, Map<String, Object> model) {
        super(errorCode, model);
    }

    public RoleException(ErrorCode errorCode, String message, Map<String, Object> model) {
        super(errorCode, message, model);
    }
}
