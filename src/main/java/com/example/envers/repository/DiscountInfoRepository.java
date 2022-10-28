package com.example.envers.repository;

import com.example.envers.entitiy.DiscountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface DiscountInfoRepository extends
  JpaRepository<DiscountInfo, Long>,
  RevisionRepository<DiscountInfo, Long, Integer> {
}
