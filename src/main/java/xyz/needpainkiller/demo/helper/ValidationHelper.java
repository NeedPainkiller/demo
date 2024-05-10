package xyz.needpainkiller.demo.helper;

import lombok.experimental.UtilityClass;
import org.apache.logging.log4j.util.Strings;
import xyz.needpainkiller.demo.api.auth.error.PasswordException;
import xyz.needpainkiller.demo.api.user.error.UserException;
import xyz.needpainkiller.demo.lib.error.BusinessException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static xyz.needpainkiller.demo.lib.error.CommonErrorCode.*;

/**
 * 검증 관련 유틸리티 클래스
 *
 * @author needpainkiller6512
 */
@UtilityClass
public class ValidationHelper {

    private static final String PATTERN_MATCH_EMAIL = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    private static final String PATTERN_MATCH_ALPHABET = ".*([a-zA-Z]).*";
    private static final String PATTERN_MATCH_NUMBERS = ".*([0-9]).*";
    private static final String PATTERN_MATCH_NUMBERS_SPECIALS = ".*([0-9`~!@#$%^&*()=_+\\\\-\\\\[\\\\]{}:;',./?\\\\\\\\|]).*";
    private static final String PATTERN_MATCH_SPECIALS = ".*([`~!@#$%^&*()=_+\\\\-\\\\[\\\\]{}:;',./?\\\\\\\\|]).*";

    /**
     * 이메일 형식인지 확인
     *
     * @param source 확인할 문자열
     * @return boolean 이메일 포맷 부합 여부
     */
    public static boolean isEmailFormat(String source) {
        Pattern p = Pattern.compile(PATTERN_MATCH_EMAIL);
        Matcher m = p.matcher(source);
        return m.matches();
    }

    /**
     * 이메일 형식인지 확인
     *
     * @param email 확인할 문자열
     * @throws BusinessException 이메일 형식이 아닌 경우
     */
    public static void checkEmailFormat(String email) throws BusinessException {
        if (!isEmailFormat(email)) {
            throw new BusinessException(USER_ID_IS_NOT_EMAIL_FORMAT);
        }
    }

    /**
     * 유저 아이디 및 이름 검증
     *
     * @param userId 유저 아이디
     *               - 4자 이상 15자 이하
     *               - 영문자 포함
     *               - 숫자 포함
     *               - 특수문자 미포함
     *               - 빈값 불가
     * @param userNm 유저 이름
     *               - 빈값 불가
     * @throws UserException 유저 아이디 및 이름 검증 실패
     */
    public static void checkUserData(String userId, String userNm) {
        if (Strings.isBlank(userId)) {
            throw new UserException(USER_ID_EMPTY);
        } else if (userId.length() < 4 || userId.length() > 15) {
            throw new UserException(USER_ID_LENGTH);
        } else if (!Pattern.matches(PATTERN_MATCH_ALPHABET, userId)) { // 문자 미포함
//            throw new UserException(USER_ID_NEED_ALPHABET); // 비활성화
        } else if (!Pattern.matches(PATTERN_MATCH_NUMBERS, userId)) { // 숫자 | 특수문자 미포함
//            throw new UserException(USER_ID_NEED_NUM); // 비활성화
        } else if (Pattern.matches(PATTERN_MATCH_SPECIALS, userId)) { // 특수문자 X
            throw new UserException(USER_ID_IGNORE_SPECIAL);
        }

        if (Strings.isBlank(userNm)) {
            throw new UserException(USER_NM_EMPTY);
        }
    }

    /**
     * 유저 아이디, 이름, 비밀번호 검증
     *
     * @param userId  유저 아이디
     * @param userNm  유저 이름
     * @param userPwd 유저 비밀번호
     * @throws UserException     유저 아이디, 이름, 비밀번호 검증 실패
     * @throws PasswordException 비밀번호 검증 실패
     */
    public static void checkUserData(String userId, String userNm, String userPwd) {
        checkUserData(userId, userNm);
        checkPassword(userPwd);
    }

    /**
     * 비밀번호 검증
     *
     * @param password 유저 비밀번호
     *                 - 10자 이상 512자 이하
     *                 - 영문자 포함
     *                 - 빈값 불가
     *                 - 숫자 또는 특수문자 포함
     *                 - 특수 문자는 `~!@#$%^&*()=_+\-[]{}:;',./?\\| 만 허용
     * @throws PasswordException 비밀번호 검증 실패
     */
    public static void checkPassword(String password) {
        if (password.length() == 0) { // 빈값
            throw new PasswordException(PASSWORD_EMPTY);
        } else if (password.length() < 10 || password.length() > 512) { // 자리수 틀림
            throw new PasswordException(PASSWORD_LENGTH);
        } else if (!Pattern.matches(PATTERN_MATCH_ALPHABET, password)) { // 문자 미포함
            throw new PasswordException(PASSWORD_NEED_ALPHABET);
        } else if (!Pattern.matches(PATTERN_MATCH_NUMBERS_SPECIALS, password)) { // 숫자 | 특수문자 미포함
            throw new PasswordException(PASSWORD_NEED_NUM_SPECIAL);
        }
    }

    /**
     * 각 문자열 파라미터가 빈값인지 확인
     *
     * @param source 확인할 문자열
     * @throws BusinessException 빈값인 경우
     */
    public static void checkAnyRequiredEmpty(String... source) {
        for (String s : source) {
            if (Strings.isBlank(s)) {
                throw new BusinessException(REQUEST_REQUIRED_DATA_EMPTY);
            }
        }
    }


}
