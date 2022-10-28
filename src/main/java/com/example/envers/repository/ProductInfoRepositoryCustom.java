package com.example.envers.repository;

import com.example.envers.entitiy.ProductInfo;
import org.springframework.data.domain.Page;

public interface ProductInfoRepositoryCustom {

  Page<ProductInfo> findAll();
}
