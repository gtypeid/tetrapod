package com.kosta.databaseserver;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.boot.jdbc.DataSourceBuilder;
import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class JPAConfig {

    private final String DRIVER_CLASS_NAME = "oracle.jdbc.driver.OracleDriver";
    private final String URL = "jdbc:oracle:thin:@localhost:1521/XE";
    private final String HIBERNATE_DIALECT = "org.hibernate.dialect.OracleDialect";

//    private final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
//    private final String URL = "jdbc:postgresql://localhost:5432/mydatabase";
//    private final String HIBERNATE_DIALECT = "org.hibernate.dialect.PostgreSQLDialect";


    @Bean
    public DataSource dataSource() {

        return DataSourceBuilder.create()
                .driverClassName(DRIVER_CLASS_NAME)
                .url(URL)
                .username("it")
                .password("0000")
                .type(com.zaxxer.hikari.HikariDataSource.class)
                .build();

//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
//        dataSource.setUrl("jdbc:oracle:thin:@localhost:1521/XE");
//        dataSource.setUsername("it");
//        dataSource.setPassword("0000");
//        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        // JPA 엔티티 클래스가 위치한 패키지
        emf.setPackagesToScan("com.kosta.common.spring.data");
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "create");
        jpaProperties.setProperty("hibernate.dialect", HIBERNATE_DIALECT);
        jpaProperties.put("hibernate.show_sql", false);

        emf.setJpaProperties(jpaProperties);
        return emf;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
