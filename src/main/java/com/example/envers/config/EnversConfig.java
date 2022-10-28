package com.example.envers.config;

import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

@Configuration
@RequiredArgsConstructor
public class EnversConfig {

  private final EntityManagerFactory entityManagerFactory;

  @Bean
  AuditReader auditReader(){
    return AuditReaderFactory.get(entityManagerFactory.createEntityManager());
  }
}
