package xyz.needpainkiller.demo.helper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.Instant;

/**
 * 시간 관련 유틸리티
 *
 * @author needpainkiller6512
 */
@UtilityClass
@Slf4j
public class TimeHelper {

    /**
     * 현재 시간을 Timestamp로 반환
     *
     * @return 현재 시간
     */
    public static Timestamp now() {
        return Timestamp.from(Instant.now());
    }
}