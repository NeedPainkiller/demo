package xyz.needpainkiller.demo.api.user.dao;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import xyz.needpainkiller.demo.api.user.model.User;

import java.util.List;

/**
 * 사용자 정보를 다루는 Repository
 */
public interface UserRepo extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    /**
     * 모든 사용자 정보를 조회한다.
     */
    List<User> findAll();

    /**
     * 고유 사용자 PK 값에 해당하는 사용자 정보를 조회한다.
     */
    User findUserById(@NotNull Long userPk);

    /**
     * 사용자 ID 값에 해당하는 사용자 정보를 조회한다.
     */
    List<User> findUserByUserId(String userId);
}