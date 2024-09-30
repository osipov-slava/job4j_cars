package ru.job4j.cars.config;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class HibernateConfiguration {

    @Bean(destroyMethod = "close")
    public SessionFactory sf() {
        String jdbcUrl = System.getenv("JDBC_URL");
        String jdbcUsername = System.getenv("JDBC_USERNAME");
        String jdbcPassword = System.getenv("JDBC_PASSWORD");
        String jdbcDriver = System.getenv("JDBC_DRIVER");

        StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
        registryBuilder.applySetting("hibernate.connection.url", jdbcUrl);
        registryBuilder.applySetting("hibernate.connection.username", jdbcUsername);
        registryBuilder.applySetting("hibernate.connection.password", jdbcPassword);
        registryBuilder.applySetting("hibernate.connection.driver_class", jdbcDriver);
        final StandardServiceRegistry registry = registryBuilder.configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

}
