package xyz.needpainkiller.demo.api.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;


/**
 * 유저 권한 맵핑 엔티티
 * - 유저와 권한을 맵핑하기 위한 엔티티
 * - User와 Role의 PK를 조합하여 사용
 *
 * @see xyz.needpainkiller.demo.api.user.model.User
 * @see xyz.needpainkiller.demo.api.user.model.Role
 * @see xyz.needpainkiller.demo.api.user.model.UserRoleMapId
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity

@Table(name = "ACCOUNT_USER_ROLE_MAP")
@IdClass(UserRoleMapId.class)
public class UserRoleMap implements Serializable {
    @Serial
    private static final long serialVersionUID = -4575954203895356191L;
    @Id
    @Column(name = "USER_PK", nullable = false, columnDefinition = "bigint default 0")
    private Long userPk;
    @Id
    @Column(name = "ROLE_PK", nullable = false, columnDefinition = "bigint default 0")
    private Long rolePk;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleMap userRole = (UserRoleMap) o;
        return Objects.equals(userPk, userRole.userPk) && Objects.equals(rolePk, userRole.rolePk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userPk, rolePk);
    }
}

