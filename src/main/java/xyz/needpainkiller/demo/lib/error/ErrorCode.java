package xyz.needpainkiller.demo.lib.error;

import org.springframework.http.HttpStatus;

/**
 * 에러 코드 인터페이스
 */
public interface ErrorCode {
    String getMessage();

    HttpStatus getStatus();

    String getCode();


}
