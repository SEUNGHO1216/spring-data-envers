package com.example.envers.entitiy;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Audited(withModifiedFlag = true)
@Table(name = "product_info")
@SQLDelete(sql = "UPDATE product_info SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class ProductInfo extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private Integer price;

  private Boolean deleted = Boolean.FALSE;

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
