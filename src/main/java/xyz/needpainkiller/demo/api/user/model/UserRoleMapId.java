package xyz.needpainkiller.demo.api.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * 유저 권한 맵핑 ID
 * - UserRoleMap 엔티티에서 유저와 권한을 맵핑하기 위한 ID
 * - User와 Role의 PK를 조합하여 사용
 *
 * @author needpainkiller6512
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleMapId implements Serializable {
    @Serial
    private static final long serialVersionUID = -8587257394928557422L;
    private Long userPk;
    private Long rolePk;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleMapId that = (UserRoleMapId) o;
        return Objects.equals(userPk, that.userPk) && Objects.equals(rolePk, that.rolePk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userPk, rolePk);
    }
}

