package xyz.needpainkiller.demo.api.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RestController;
import xyz.needpainkiller.demo.api.CommonController;
import xyz.needpainkiller.demo.api.auth.dto.AuthenticationRequests;
import xyz.needpainkiller.demo.api.auth.error.LoginException;
import xyz.needpainkiller.demo.api.auth.service.AuthenticationService;
import xyz.needpainkiller.demo.api.user.dto.SecurityUser;
import xyz.needpainkiller.demo.api.user.dto.UserProfile;
import xyz.needpainkiller.demo.api.user.model.Role;
import xyz.needpainkiller.demo.api.user.model.User;
import xyz.needpainkiller.demo.api.user.service.UserService;
import xyz.needpainkiller.demo.lib.error.ErrorCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;
import static xyz.needpainkiller.demo.lib.error.CommonErrorCode.LOGIN_REQUEST_MISSING;

/**
 * 로그인 및 토큰 조회 컨트롤러
 *
 * @author needpainkiller6512
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController extends CommonController implements AuthenticationApi {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;

    /**
     * 로그인 & 토큰 발급
     *
     * @param param    AuthenticationRequests.LoginRequest
     *                 - userId: 사용자 ID
     *                 - userPwd: 사용자 비밀번호
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return ResponseEntity<Map < String, Object>> 응답 데이터
     * @throws LoginException 로그인 예외
     */
    @Override
    public ResponseEntity<Map<String, Object>> login(AuthenticationRequests.LoginRequest param, HttpServletRequest request, HttpServletResponse response) throws LoginException {

        String userId = param.getUserId();
        String userPwd = param.getUserPwd();
        if (Strings.isBlank(userId) || Strings.isBlank(userPwd)) {
            throw new LoginException(LOGIN_REQUEST_MISSING);
        }
        userId = userId.trim();
        userPwd = userPwd.trim();

        SecurityUser securityUser;
        User user = userService.selectUserByUserId(userId);

        try {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userId, userPwd);
            Authentication authentication = authenticationManager.authenticate(token);
            securityUser = (SecurityUser) authentication.getPrincipal();
        } catch (AuthenticationException e) {
            userService.increaseLoginFailedCnt(user.getId());
            ErrorCode errorCode = authenticationService.getAuthenticationExceptionType(user, e);
            throw new LoginException(errorCode, e.getMessage());
        }
        user = securityUser.getUser();
        Map<String, Object> model = new HashMap<>();
        List<Role> roleList = null;
        try {
            UserProfile userProfile = userService.selectUserProfile(user);
            model.put(KEY_USER, user);
            roleList = userProfile.getRoleList();
            model.put(KEY_ROLE_LIST, roleList);
        } finally {
            model.put(KEY_TOKEN, authenticationService.createToken(user, roleList));
            userService.updateLastLoginDate(user.getId());
        }
        return ok(model);
    }

    /**
     * 토큰 재발급
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return ResponseEntity<Map < String, Object>> 응답 데이터
     */
    @Override
    public ResponseEntity<Map<String, Object>> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = authenticationService.refreshToken(request);
        Map<String, Object> model = new HashMap<>();
        model.put(KEY_TOKEN, refreshToken);
        return ok(model);
    }

    /**
     * 토큰 검증
     *
     * @param request HttpServletRequest
     * @return ResponseEntity<Map < String, Object>> 응답 데이터
     */
    @Override
    public ResponseEntity<Map<String, Object>> validateToken(HttpServletRequest request) {
        authenticationService.validateToken(request);
        return ok().build();
    }
}
