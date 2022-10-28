package com.example.envers.service;

import com.example.envers.entitiy.ProductInfo;
import com.example.envers.repository.ProductInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductInfoService {

  private final ProductInfoRepository productInfoRepository;
  private final EntityManager entityManager;
  private final AuditReader auditReader;

  @Transactional(readOnly = true)
  public List<ProductInfo> pagingTestByAuditReader(Long productId) {
    return auditReader.createQuery()
      .forRevisionsOfEntity(ProductInfo.class, true, true)
      .add(AuditEntity.id().eq(productId))
      .addOrder(AuditEntity.property("modifiedDate").desc())
      .getResultList();
  }
}
