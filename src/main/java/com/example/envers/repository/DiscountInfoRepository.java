package com.example.envers.repository;

import com.example.envers.entitiy.DiscountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.List;

public interface DiscountInfoRepository extends
  JpaRepository<DiscountInfo, Long>,
  RevisionRepository<DiscountInfo, Long, Integer> {

  void deleteAllByProductId(Long productId);

  List<DiscountInfo> findAllByProductId(Long productId);
}
