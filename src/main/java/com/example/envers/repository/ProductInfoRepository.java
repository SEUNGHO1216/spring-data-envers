package com.example.envers.repository;

import com.example.envers.entitiy.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface ProductInfoRepository extends
  JpaRepository<ProductInfo, Long>,
  RevisionRepository<ProductInfo, Long, Integer> {
}
