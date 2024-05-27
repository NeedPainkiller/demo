package xyz.needpainkiller.demo.api.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import xyz.needpainkiller.demo.api.CommonController;
import xyz.needpainkiller.demo.api.SearchCollectionResult;
import xyz.needpainkiller.demo.api.auth.service.AuthenticationService;
import xyz.needpainkiller.demo.api.user.dto.UserProfile;
import xyz.needpainkiller.demo.api.user.dto.UserRequests;
import xyz.needpainkiller.demo.api.user.error.RoleException;
import xyz.needpainkiller.demo.api.user.error.UserException;
import xyz.needpainkiller.demo.api.user.model.Role;
import xyz.needpainkiller.demo.api.user.model.User;
import xyz.needpainkiller.demo.api.user.service.RoleService;
import xyz.needpainkiller.demo.api.user.service.UserService;
import xyz.needpainkiller.demo.helper.ValidationHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;
import static xyz.needpainkiller.demo.lib.error.CommonErrorCode.ROLE_NOT_EXIST;
import static xyz.needpainkiller.demo.lib.error.CommonErrorCode.USER_DELETE_SELF;

/**
 * 계정 관리 컨트롤러
 *
 * @author needpainkiller6512
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController extends CommonController implements UserApi {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;


    /**
     * 유저 리스트 조회
     *
     * @param request HttpServletRequest
     * @return ResponseEntity<Map < String, Object>> 응답 데이터
     */
    @Override
    public ResponseEntity<Map<String, Object>> selectUserList(HttpServletRequest request) {
        Map<String, Object> model = new HashMap<>();
        SearchCollectionResult<UserProfile> result = userService.selectUserProfileList();
        model.put(KEY_LIST, result.getCollection());
        model.put(KEY_TOTAL, result.getFoundRows());
        return ok(model);
    }

    /**
     * 유저 조회
     *
     * @param userPk  유저 고유번호
     * @param request HttpServletRequest
     * @return ResponseEntity<Map < String, Object>> 응답 데이터
     */
    @Override
    public ResponseEntity<Map<String, Object>> selectUser(Long userPk, HttpServletRequest request) {
        Map<String, Object> model = new HashMap<>();
        UserProfile userProfile = userService.selectUserProfile(userPk);
        model.put(KEY_USER, userProfile);
        return ok(model);
    }

    /**
     * 내 정보 조회
     *
     * @param request HttpServletRequest
     * @return ResponseEntity<Map < String, Object>> 응답 데이터
     */
    @Override
    public ResponseEntity<Map<String, Object>> selectMe(HttpServletRequest request) {
        Map<String, Object> model = new HashMap<>();
        User user = authenticationService.getUserByToken(request);
        UserProfile userProfile = userService.selectUserProfile(user);
        model.put(KEY_USER, userProfile);
        return ok(model);
    }

    /**
     * 유저 Id 중복확인
     *
     * @param userId  유저 아이디
     * @param request HttpServletRequest
     * @return ResponseEntity<Map < String, Object>> 응답 데이터
     */
    @Override
    public ResponseEntity<Map<String, Object>> isUserIdExist(String userId, HttpServletRequest request) {
        Map<String, Object> model = new HashMap<>();
        ValidationHelper.checkAnyRequiredEmpty(userId);
        model.put(KEY_EXIST, userService.isUserIdExist(userId));
        return ok(model);
    }

    /**
     * 유저 회원가입
     *
     * @param param   회원가입 정보
     * @param request HttpServletRequest
     * @return ResponseEntity<Map < String, Object>> 응답 데이터
     */
    @Override
    public ResponseEntity<Map<String, Object>> signUpUser(UserRequests.SignUpUserRequest param, HttpServletRequest request) {
        Map<String, Object> model = new HashMap<>();

        User savedUser = userService.signupUser(param);
        UserProfile userProfile = userService.selectUserProfile(savedUser);
        model.put(KEY_USER, userProfile);
        return status(HttpStatus.CREATED).body(model);
    }

    /**
     * 유저등록
     *
     * @param param   유저 정보
     * @param request HttpServletRequest
     * @return ResponseEntity<Map < String, Object>> 응답 데이터
     */
    @Override
    public ResponseEntity<Map<String, Object>> createUser(UserRequests.UpsertUserRequest param, HttpServletRequest request) {
        Map<String, Object> model = new HashMap<>();

        List<Role> requestRoleList = roleService.selectRolesByPkList(param.getRoles());
        if (requestRoleList.isEmpty()) {
            throw new RoleException(ROLE_NOT_EXIST);
        }

        User requester = authenticationService.getUserByToken(request);
        List<Role> authority = authenticationService.getRoleListByToken(request);
        roleService.checkRequestRoleAuthority(requestRoleList, authority);

        User savedUser = userService.createUser(param, requestRoleList, requester);
        UserProfile userProfile = userService.selectUserProfile(savedUser);
        model.put(KEY_USER, userProfile);
        return status(HttpStatus.CREATED).body(model);
    }

    /**
     * 유저 정보 변경
     *
     * @param userPk  유저 고유번호
     * @param param   변경할 유저 정보
     * @param request HttpServletRequest
     * @return ResponseEntity<Map < String, Object>> 응답 데이터
     */
    @Override
    public ResponseEntity<Map<String, Object>> updateUser(Long userPk, UserRequests.UpsertUserRequest param, HttpServletRequest request) {
        Map<String, Object> model = new HashMap<>();
        List<Role> requestRoleList = roleService.selectRolesByPkList(param.getRoles());
        if (requestRoleList.isEmpty()) {
            throw new RoleException(ROLE_NOT_EXIST);
        }
        User requester = authenticationService.getUserByToken(request);
        List<Role> authority = authenticationService.getRoleListByToken(request);
        roleService.checkRequestRoleAuthority(requestRoleList, authority);

        User savedUser = userService.updateUser(userPk, param, requestRoleList, requester);
        UserProfile userDetail = userService.selectUserProfile(savedUser);
        model.put(KEY_USER, userDetail);
        return status(HttpStatus.CREATED).body(model);
    }

    /**
     * 유저 삭제
     *
     * @param userPk  유저 고유번호
     * @param request HttpServletRequest
     * @return ResponseEntity<Map < String, Object>> 응답 데이터
     */
    @Override
    public ResponseEntity<Map<String, Object>> deleteUser(Long userPk, HttpServletRequest request) throws UserException {
        Map<String, Object> model = new HashMap<>();
        User requester = authenticationService.getUserByToken(request);
        if (userPk.equals(requester.getId())) {
            throw new UserException(USER_DELETE_SELF);
        }
        userService.deleteUser(userPk, requester);
        return status(HttpStatus.NO_CONTENT).body(model);
    }
}
