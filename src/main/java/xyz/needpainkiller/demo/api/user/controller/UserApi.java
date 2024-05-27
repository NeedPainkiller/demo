package xyz.needpainkiller.demo.api.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.needpainkiller.demo.api.user.dto.UserRequests;

import java.util.Map;

/**
 * 계정 관리 컨트롤러 인터페이스
 * - OpenAPI 3.0 스펙에 맞춰 작성
 *
 * @author needpainkiller6512
 */
@Tag(name = "계정 관리", description = "USER")
@RequestMapping(value = "/api/v1/user", produces = {MediaType.APPLICATION_JSON_VALUE})
public interface UserApi {

    @Operation(description = "유저 리스트 조회")
    @GetMapping(value = "/list")
    ResponseEntity<Map<String, Object>> selectUserList(HttpServletRequest request);


    @Operation(description = "유저 조회")
    @GetMapping(value = "/{userPk}")
    ResponseEntity<Map<String, Object>> selectUser(@PathVariable("userPk") Long userPk, HttpServletRequest request);

    @Operation(description = "내 정보 조회")
    @GetMapping(value = "/me")
    ResponseEntity<Map<String, Object>> selectMe(HttpServletRequest request);

    @Operation(description = "유저 Id  중복확인")
    @GetMapping(value = "/exists/{userId}")
    ResponseEntity<Map<String, Object>> isUserIdExist(@NotBlank @PathVariable("userId") String userId, HttpServletRequest request);

    @PostMapping(value = "/sign-up")
    @Operation(description = "유저 회원가입")
    ResponseEntity<Map<String, Object>> signUpUser(@Valid @RequestBody UserRequests.SignUpUserRequest param, HttpServletRequest request);

    @PostMapping(value = "")
    @Operation(description = "유저등록")
    ResponseEntity<Map<String, Object>> createUser(@Valid @RequestBody UserRequests.UpsertUserRequest param, HttpServletRequest request);

    @PutMapping(value = "/{userPk}")
    @Operation(description = "유저 정보 변경")
    ResponseEntity<Map<String, Object>> updateUser(@NotNull @PathVariable("userPk") Long userPk, @Valid @RequestBody UserRequests.UpsertUserRequest param, HttpServletRequest request);

    @DeleteMapping(value = "/{userPk}")
    @Operation(description = "유저 삭제")
    ResponseEntity<Map<String, Object>> deleteUser(@NotNull @PathVariable("userPk") Long userPk, HttpServletRequest request);

}