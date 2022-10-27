package com.example.envers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.envers.repository.config.EnableEnversRepositories;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Random;

@EnableJpaAuditing
@EnableEnversRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@SpringBootApplication
public class EnversApplication {

  @Bean
  public AuditorAware<String> auditorProvider() {
    return () -> java.util.Optional.of("이름[" + new Random().nextInt(100) + "]");
  }

  public static void main(String[] args) {
    SpringApplication.run(EnversApplication.class, args);
  }

}
