package xyz.needpainkiller.demo.api.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import xyz.needpainkiller.demo.lib.jpa.BooleanConverter;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * 권한 엔티티
 * - Spring Security에서 사용하는 권한 엔티티 (GrantedAuthority)
 *
 * @author needpainkiller6512
 */
@Getter
@Setter
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "ACCOUNT_ROLE")
public class Role implements GrantedAuthority, Serializable {
    @Serial
    private static final long serialVersionUID = 517080619145517610L;

    /**
     * 권한 고유 번호 - PK
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_ID", unique = true, nullable = false, columnDefinition = "bigint")
    private Long id;

    /**
     * 권한 활성화 여부
     */
    @Convert(converter = BooleanConverter.class)
    @Column(name = "USE_YN", nullable = false, columnDefinition = "tinyint unsigned default 0")
    private boolean useYn;

    /**
     * 관리자 여부
     */
    @Convert(converter = BooleanConverter.class)
    @Column(name = "IS_ADMIN", nullable = false, columnDefinition = "tinyint unsigned default 0")
    private boolean isAdmin;

    /**
     * 권한 이름
     */
    @Column(name = "ROLE_NAME", nullable = false, columnDefinition = "nvarchar(256)")
    private String roleName;

    /**
     * 권한 설명
     */
    @Column(name = "ROLE_DESCRIPTION", nullable = true, columnDefinition = "nvarchar(1024) default null")
    private String roleDescription;

    /**
     * 생성자
     * - 0은 시스템 생성
     */
    @Column(name = "CREATED_BY", nullable = false, columnDefinition = "bigint default 0")
    private Long createdBy;

    /**
     * 생성일
     * - 등록 시 당시 시간으로 자동 생성
     */
    @Column(name = "CREATED_DATE", nullable = false, columnDefinition = "datetime2(0) default CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Timestamp createdDate;

    /**
     * 수정자
     */
    @Column(name = "UPDATED_BY", nullable = false, columnDefinition = "bigint default 0")
    private Long updatedBy;

    /**
     * 수정일
     * - 현재 시간으로 자동 생성
     */
    @Column(name = "UPDATED_DATE", nullable = false, columnDefinition = "datetime2(0) default CURRENT_TIMESTAMP")
    @UpdateTimestamp
    private Timestamp updatedDate;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (useYn != role.useYn) return false;
        if (isAdmin != role.isAdmin) return false;
        if (!Objects.equals(id, role.id)) return false;
        if (!Objects.equals(roleName, role.roleName)) return false;
        if (!Objects.equals(roleDescription, role.roleDescription))
            return false;
        if (!Objects.equals(createdBy, role.createdBy)) return false;
        if (!Objects.equals(createdDate, role.createdDate)) return false;
        if (!Objects.equals(updatedBy, role.updatedBy)) return false;
        return Objects.equals(updatedDate, role.updatedDate);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (useYn ? 1 : 0);
        result = 31 * result + (isAdmin ? 1 : 0);
        result = 31 * result + (roleName != null ? roleName.hashCode() : 0);
        result = 31 * result + (roleDescription != null ? roleDescription.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedBy != null ? updatedBy.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String sb = "Role{" + "id=" + id +
                ", useYn=" + useYn +
                ", isAdmin=" + isAdmin +
                ", roleName='" + roleName + '\'' +
                ", roleDescription='" + roleDescription + '\'' +
                ", createdBy=" + createdBy +
                ", createdDate=" + createdDate +
                ", updatedBy=" + updatedBy +
                ", updatedDate=" + updatedDate +
                '}';
        return sb;
    }

    /**
     * Spring Security에서 사용하는 권한 이름
     * - GrantedAuthority 인터페이스 구현
     */
    @Override
    public String getAuthority() {
        return roleName;
    }

    /**
     * 사용 가능 한 권한 여부
     */
    public boolean isAvailable() {
        return this.useYn;
    }

    /**
     * 관리자 여부
     */
    public boolean isAdmin() {
        return this.isAdmin;
    }


}
