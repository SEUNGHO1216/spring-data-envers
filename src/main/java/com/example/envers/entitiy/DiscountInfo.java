package com.example.envers.entitiy;

import com.querydsl.core.annotations.QueryEntities;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Audited(withModifiedFlag = true)
@Table(name = "discount_info")
@SQLDelete(sql = "UPDATE discount_info SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class DiscountInfo extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private Integer discountPrice;

  @Nullable
  private Long productId;

  private Boolean deleted = Boolean.FALSE;

  public static DiscountInfo create(String name, Integer discountPrice, @Nullable Long productId){
    DiscountInfo discountInfo = new DiscountInfo();
    discountInfo.setName(name);
    discountInfo.setDiscountPrice(discountPrice);
    discountInfo.setProductId(productId);
    return discountInfo;
  }

  public DiscountInfo update(String name, Integer discountPrice, @Nullable Long productId){
    this.setName(name);
    this.setDiscountPrice(discountPrice);
    this.setProductId(productId);
    return this;
  }

  public DiscountInfo applyToProduct(Long targetProductId){
    this.setProductId(targetProductId);
    return this;
  }

  public DiscountInfo recover(){
    this.setDeleted(true);
    return this;
  }
}
