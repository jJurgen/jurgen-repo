package com.jurgen.chat.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan("com.jurgen.chat")
@EnableTransactionManagement
@Import({SpringSecurityConfig.class})
public class ApplicationContextConfig {

    @Bean
    public DataSource getDataSource() {
        BasicDataSource datasource = new BasicDataSource();
        datasource.setDriverClassName("com.mysql.jdbc.Driver");
        datasource.setUrl("jdbc:mysql://localhost/chatdb");
        datasource.setUsername("root");
        datasource.setPassword("123456");
        return datasource;
    }

    @Autowired
    @Bean
    public SessionFactory getSessionFactory(DataSource dataSource) {
        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
        sessionBuilder.scanPackages("com.jurgen.chat.domain");
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        hibernateProperties.put("hibernate.show_sql", "true");
        hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
        sessionBuilder.addProperties(hibernateProperties);
        return sessionBuilder.buildSessionFactory();
    }

}
