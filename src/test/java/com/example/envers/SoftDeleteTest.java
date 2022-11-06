package com.example.envers;

import com.example.envers.entitiy.DiscountInfo;
import com.example.envers.entitiy.ProductInfo;
import com.example.envers.repository.DiscountInfoRepository;
import com.example.envers.repository.ProductInfoRepository;
import com.example.envers.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class SoftDeleteTest {

  @Autowired
  private ProductInfoService productInfoService;
  @Autowired
  private ProductInfoRepository productInfoRepository;
  @Autowired
  private DiscountInfoRepository discountInfoRepository;
  @Autowired
  private AuditReader auditReader;

  @Test
  @Rollback(value = false)
  public void 상품_할인_생성(){
    List<ProductInfo> productInfoList = new ArrayList<>();
    List<DiscountInfo> discountInfoList = new ArrayList<>();
    for(int i = 0 ;i < 3 ; i++){
      productInfoList.add(ProductInfo.create("아이스아메리카노"+i,2500+i));
      discountInfoList.add(DiscountInfo.create(i+"% 할인", i, null));
    }
    productInfoRepository.saveAll(productInfoList);
    discountInfoRepository.saveAll(discountInfoList);
  }

  @Test
  @Rollback(value = false)
  public void 상품에_할인적용(){
    Long targetProductId = 2L;
    Long fromDiscountId = 3L;

    //이때 findById 에서도 where deleted = false 조건이 나감
    discountInfoRepository.findById(fromDiscountId).get().applyToProduct(targetProductId);
//    discountInfoRepository.findById(fromDiscountId+1).get().applyToProduct(targetProductId);
//    discountInfoRepository.findById(fromDiscountId+2).get().applyToProduct(targetProductId);
  }

  @Test
  @Rollback(value = false)
  public void 상품삭제_deleteById(){
    Long targetProductId = 2L;

    productInfoRepository.deleteById(targetProductId);
    ProductInfo productInfo = productInfoRepository.findById(targetProductId).get();

    assertEquals(true, productInfo.getDeleted());
  }

  @Test
  @Rollback(value = false)
  public void 할인삭제_deleteAllById(){
    Long targetProductId = 1L;

    discountInfoRepository.deleteAllByProductId(targetProductId);
  }

  @Test
  @Rollback(value = false)
  public void 상품조회(){
    Optional<ProductInfo> productInfo = productInfoRepository.findById(2L);
    log.info("삭제된 상품은 나올까요? >>"+productInfo);

    List<ProductInfo> productInfoList = productInfoRepository.findAll();
    assertEquals(2, productInfoList.size());
    assertEquals(3, productInfoList.get(1).getId()); // 삭제된 데이터 안 나옴
  }

  @Test
  @Rollback(value = false)
  public void 할인조회(){
    discountInfoRepository.deleteAllByProductId(1L);

    assertEquals(null, discountInfoRepository.findById(1L));
  }

  @Test
  @Rollback(value = false)
  public void 할인복구(){
    List<DiscountInfo> discountInfoList = discountInfoRepository.findAllByProductId(2L);
    for(DiscountInfo one : discountInfoList){
//      assert one.getProductId() != null;
      Optional<ProductInfo> productInfo = productInfoRepository.findById(one.getProductId());
      if(productInfo.isPresent()){
        log.info("존재함");
      }else{
        log.info("삭제된 데이터");
      }
    }
  }
}