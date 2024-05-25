package xyz.needpainkiller.demo.api.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import xyz.needpainkiller.demo.lib.jpa.BooleanConverter;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * 유저 엔티티
 *
 * @author needpainkiller6512
 * @see xyz.needpainkiller.demo.api.user.model.UserStatusType
 */
@Setter
@NoArgsConstructor
@JsonInclude(NON_NULL)
@Entity
@DynamicUpdate
@Table(name = "ACCOUNT_USER")
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = -8737333234871506911L;

    /**
     * 유저 고유 번호 - PK
     */
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_ID", unique = true, nullable = false, columnDefinition = "bigint")
    private Long id;

    /**
     * 유저 활성화 여부
     */
    @Convert(converter = BooleanConverter.class)
    @Column(name = "USE_YN", nullable = false, columnDefinition = "tinyint unsigned default 0")
    @Getter
    private boolean useYn;

    /**
     * 유저 아이디
     */
    @Column(name = "USER_ID", nullable = false, columnDefinition = "nvarchar(256)")
    @Getter
    private String userId;

    /**
     * 유저 이메일
     */
    @Column(name = "USER_EMAIL", nullable = true, columnDefinition = "nvarchar(256) default null")
    @Getter
    private String userEmail;

    /**
     * 유저 이름
     */
    @Column(name = "USER_NAME", nullable = false, columnDefinition = "nvarchar(256)")
    @Getter
    private String userName;

    /**
     * 유저 패스워드
     * - 로깅 또는 클라이언트에 노출되지 않도록 주의
     * - Getter는 null을 반환하도록 설정
     */
    @Column(name = "USER_PWD", nullable = false, columnDefinition = "nvarchar(1024)")
    private String userPwd;


    /**
     * 유저 상태 타입
     * - UserStatusType Enum 참조
     * - JPA Enum 타입 변환기 사용
     */
    @Convert(converter = UserStatusType.Converter.class)
    @Column(name = "USER_STATUS", nullable = false, columnDefinition = "int unsigned default 0")
    @Getter
    private UserStatusType userStatus;

    /**
     * 해당 유저 생성자
     * - 0은 시스템 생성 (회원 가입 등)
     */
    @Column(name = "CREATED_BY", nullable = false, columnDefinition = "bigint default 0")
    @Getter
    private Long createdBy;

    /**
     * 해당 유저 생성일
     * - 등록 시 당시 시간으로 자동 생성
     */
    @Column(name = "CREATED_DATE", nullable = false, columnDefinition = "datetime2(0) default CURRENT_TIMESTAMP")
    @Getter
    @CreationTimestamp
    private Timestamp createdDate;

    /**
     * 해당 유저 수정자
     * - 0은 시스템 수정 (회원 정보 수정 등)
     */
    @Column(name = "UPDATED_BY", nullable = false, columnDefinition = "bigint default 0")
    @Getter
    private Long updatedBy;

    /**
     * 해당 유저 수정일
     * - 현재 시간으로 자동 생성
     */
    @Column(name = "UPDATED_DATE", nullable = false, columnDefinition = "datetime2(0) default CURRENT_TIMESTAMP")
    @Getter
    private Timestamp updatedDate;

    /**
     * 로그인 실패 횟수
     * - 로그인 실패 시 증가
     * - 로그인 성공 시 0으로 초기화
     */
    @Column(name = "LOGIN_FAILED_CNT", nullable = false, columnDefinition = "int unsigned default 0")
    @Getter
    private Integer loginFailedCnt;

    /**
     * 마지막 로그인 일시
     * - 로그인 성공 시 갱신
     */
    @Column(name = "LAST_LOGIN_DATE", nullable = true, columnDefinition = "datetime2(0) default null")
    @Getter
    private Timestamp lastLoginDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (useYn != user.useYn) return false;
        if (!Objects.equals(id, user.id)) return false;
        if (!Objects.equals(userId, user.userId)) return false;
        if (!Objects.equals(userEmail, user.userEmail)) return false;
        if (!Objects.equals(userName, user.userName)) return false;
        if (userStatus != user.userStatus) return false;
        if (!Objects.equals(createdBy, user.createdBy)) return false;
        if (!Objects.equals(createdDate, user.createdDate)) return false;
        if (!Objects.equals(updatedBy, user.updatedBy)) return false;
        if (!Objects.equals(updatedDate, user.updatedDate)) return false;
        if (!Objects.equals(loginFailedCnt, user.loginFailedCnt))
            return false;
        return Objects.equals(lastLoginDate, user.lastLoginDate);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (useYn ? 1 : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (userEmail != null ? userEmail.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (userStatus != null ? userStatus.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (updatedBy != null ? updatedBy.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        result = 31 * result + (loginFailedCnt != null ? loginFailedCnt.hashCode() : 0);
        result = 31 * result + (lastLoginDate != null ? lastLoginDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String sb = "User{" + "id=" + id +
                ", useYn=" + useYn +
                ", userId='" + userId + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userName='" + userName + '\'' +
                ", userStatus=" + userStatus +
                ", createdBy=" + createdBy +
                ", createdDate=" + createdDate +
                ", updatedBy=" + updatedBy +
                ", updatedDate=" + updatedDate +
                ", loginFailedCnt=" + loginFailedCnt +
                ", lastLoginDate=" + lastLoginDate +
                '}';
        return sb;
    }

    /**
     * 로그인 가능한 상태 인지 확인
     *
     * @return : 로그인 가능 여부
     */
    @JsonIgnore
    public boolean isLoginEnabled() {
        return this.useYn && this.userStatus.isLoginable();
    }

    /**
     * 사용 가능한 상태 인지 확인
     *
     * @return : 사용 가능 여부
     */
    @JsonIgnore
    public boolean isAvailable() {
        return this.useYn;
    }

    /**
     * 패스워드 Getter
     * - 클라이언트에 노출되지 않도록 설정
     * - Transient로 설정하여 JPA 엔티티에 포함되지 않도록 설정
     */
    @Transient
    @JsonIgnore
    // password is not exposed to the client
    public String getUserPwd() {
        return null;
    }

    /**
     * 패스워드 조회
     * - 클라이언트에 노출되지 않도록 설정
     * - Transient로 설정하여 JPA 엔티티에 포함되지 않도록 설정
     * - SecurityUser의 password로 사용
     */
    @Transient
    @JsonIgnore
    // password for SecurityUser
    public final String pwd() {
        return this.userPwd;
    }
}
