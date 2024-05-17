package xyz.needpainkiller.demo.api;

import lombok.extern.slf4j.Slf4j;

/**
 * 기본 컨트롤러 클래스
 * 컨트롤러 응답에 사용되는 키값을 정의
 *
 * @author needpainkiller6512
 */
@Slf4j
public abstract class CommonController {
    protected static final String KEY_OPTION = "option";
    protected static final String KEY_RESULT = "result";
    protected static final String KEY_MESSAGE = "message";
    protected static final String KEY_LIST = "list";
    protected static final String KEY_MAP = "map";
    protected static final String KEY_TOTAL = "total";

    protected static final String KEY_EXIST = "exist";

    protected static final String KEY_USER = "user";
    protected static final String KEY_ROLE = "role";
    protected static final String KEY_ROLE_LIST = "roleList";
    protected static final String KEY_FILE_LIST = "fileList";
    protected static final String KEY_TOKEN = "token";
}