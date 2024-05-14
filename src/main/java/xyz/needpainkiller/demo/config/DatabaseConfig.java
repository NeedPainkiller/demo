package xyz.needpainkiller.demo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


/**
 * 데이터벵이스 Datasource 설정
 *
 * @author needpainkiller6512
 */
@Slf4j
@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = {"xyz.needpainkiller.**.model"})
@EnableJpaRepositories(
        bootstrapMode = BootstrapMode.DEFERRED,
        basePackages = {"xyz.needpainkiller.**.dao"},
        transactionManagerRef = "jpaTransactionManager"
)
public class DatabaseConfig {

    /**
     * HikariCP : Connection Pool 라이브러리
     * HikariConfig : Connection Pool 설정을 위한 클래스
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    /**
     * Datasource : Connection Pool을 지원하는 인터페이스
     */
    @Primary
    @Bean
    public DataSource dataSource() {
        DataSource dataSource = new HikariDataSource(hikariConfig());
        log.info("datasource :{}", dataSource);
        return dataSource;
    }

    /**
     * LocalContainerEntityManagerFactoryBean
     * EntityManager를 생성하는 팩토리
     * SessionFactoryBean과 동일한 역할, Datasource와 mapper를 스캔할 .xml 경로를 지정하듯이
     * datasource와 엔티티가 저장된 폴더 경로를 매핑해주면 된다.
     *
     * @param dataSource
     * @return
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("xyz.needpainkiller.**.model");
        emf.setPersistenceUnitName("entityManager");
        emf.setJpaVendorAdapter(vendorAdapter);
        return emf;
    }

    /**
     * JpaTransactionManager
     * JPA를 사용할 때 트랜잭션을 관리하는 클래스
     *
     * @param emf
     * @return
     */
    @Primary
    @Bean
    public JpaTransactionManager jpaTransactionManager(LocalContainerEntityManagerFactoryBean emf) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(emf.getObject());
        return jpaTransactionManager;
    }

}
