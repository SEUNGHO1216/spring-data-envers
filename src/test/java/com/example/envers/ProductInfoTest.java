package com.example.envers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductInfoTest {

  @Autowired
  private ProductInfoRepository productInfoRepository;
  @Autowired
  private DiscountInfoRepository discountInfoRepository;

  @Test
  @Rollback(value = false)
  public void createProductTest() {
    ProductInfo productInfo = new ProductInfo(null, "상품1", 20000);
    productInfoRepository.save(productInfo);
    DiscountInfo discountInfo = new DiscountInfo(null, "할인1", 2000, null);
    discountInfoRepository.save(discountInfo);
  }

  @Test
  @Rollback(value = false)
  public void updateProductTest(){
    productInfoRepository.findById(1L).get().update("상품수정!",25000);
    discountInfoRepository.findById(2L).get().update("할인수정!", 3000, null);
  }

  @Test
  @Rollback(value = false)
  public void applyDiscountToProduct(){
    discountInfoRepository.findById(2L).get().applyToProduct(1L);
  }

  @Test
  @Rollback(value = false)
  public void 생성과_할인적용_트랜잭션(){
    ProductInfo productInfo = new ProductInfo(null, "상품3", 10000);
    Long createdId = productInfoRepository.save(productInfo).getId();
    discountInfoRepository.findById(1L).get().applyToProduct(createdId);
  }

}