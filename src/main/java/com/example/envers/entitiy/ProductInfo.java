package com.example.envers.entitiy;

import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Audited(withModifiedFlag = true)
public class ProductInfo extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private Integer price;

  public static ProductInfo create(String name, Integer price){
    ProductInfo productInfo = new ProductInfo();
    productInfo.setName(name);
    productInfo.setPrice(price);
    return productInfo;
  }

  public ProductInfo update(String name, Integer price){
    this.setName(name);
    this.setPrice(price);
    return this;
  }
}
