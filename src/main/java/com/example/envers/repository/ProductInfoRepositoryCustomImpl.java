package com.example.envers.repository;

import com.example.envers.entitiy.ProductInfo;
import com.example.envers.entitiy.QProductInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.history.support.RevisionEntityInformation;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductInfoRepositoryCustomImpl implements ProductInfoRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;
  private final QProductInfo productInfo = QProductInfo.productInfo;

  @Override
  public Page<ProductInfo> findAll() {
    return null;
  }
}
