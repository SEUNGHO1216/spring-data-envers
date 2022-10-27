package com.example.envers;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface DiscountInfoRepository extends
  JpaRepository<DiscountInfo, Long>,
  RevisionRepository<DiscountInfo, Long, Integer> {
}
