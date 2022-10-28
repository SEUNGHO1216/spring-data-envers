package com.example.envers;

import com.example.envers.entitiy.DiscountInfo;
import com.example.envers.entitiy.ProductInfo;
import com.example.envers.repository.DiscountInfoRepository;
import com.example.envers.repository.ProductInfoRepository;
import com.example.envers.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.MatchMode;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
@Transactional
@Slf4j
class ProductInfoTest {

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
  public void createProductAndDiscountTest() {
    // 상품 한개 만들기
    ProductInfo productInfo = new ProductInfo(null, "상품4", 12000);
    productInfoRepository.save(productInfo);
    // 할인 한개 만들기
    DiscountInfo discountInfo = new DiscountInfo(null, "할인3", 4000, null);
    discountInfoRepository.save(discountInfo);
  }

  @Test
  @Rollback(value = false)
  public void updateProductAndDiscountTest() {
    // 1번 상품 수정
    productInfoRepository.findById(2L).get().update("상품수정~~22", 190000);
    // 2번 할인 수정
    discountInfoRepository.findById(2L).get().update("할인수정!", 10000, null);
  }

  @Test
  @Rollback(value = false)
  public void applyDiscountToProduct() {
    // 2번 할인을 1번 상품에 적용(즉 2번 수정)
    discountInfoRepository.findById(2L).get().applyToProduct(1L);
  }

  @Test
  @Rollback(value = false)
  public void 생성과_할인적용_트랜잭션() {
    // 상품 한개 만들기
    ProductInfo productInfo = new ProductInfo(null, "상품_생성과할인", 10000);
    Long createdId = productInfoRepository.save(productInfo).getId();
    // 1번 할인을 생성된 상품에 바로 적용(즉 한 트랜잭션에 생성과 수정이 같은 내역 번호로 들어가나 확인)
    discountInfoRepository.findById(1L).get().applyToProduct(createdId);
  }

  @Test
  public void getFindRevisions() {
    //한 엔티티 객체의 모든 내역 정보 가져오기(default 오름차순)
    Revisions<Integer, ProductInfo> revisions = productInfoRepository.findRevisions(2L);
    //revisions.getLatestRevision() => 여러 내역 중 가장 최근 revision 정보 반환
    Revision<Integer, ProductInfo> latestRevision = revisions.getLatestRevision();
    log.info("latestRevision.getRequiredRevisionNumber() >> {}", latestRevision.getRequiredRevisionNumber());
    log.info("latestRevision.getRequiredRevisionInstant() >> {}", latestRevision.getRequiredRevisionInstant());
    //revisions.getContent() => 리비전을 리스트로 만듦
    //만약 content가 여러개일 경우 오름차순으로 정렬(내림차순으로 보고 싶다면 findRevisions에서 reverse())
    List<Revision<Integer, ProductInfo>> content = revisions.getContent();
    content.stream().forEach(result -> {
        //해당 엔티티를 반환 ProductInfo productInfo = ~
        log.info("result.getEntity() >>" + result.getEntity());
        //----- metadata 부분 -----///
        //optional로 리버전 넘버 반환(트랜잭션 번호 o, 타입 x)
        log.info("result.getMetadata().getRevisionNumber() >>" + result.getMetadata().getRevisionNumber()); //Optional
        log.info("result.getMetadata().getRevisionNumber().get() >>" + result.getMetadata().getRevisionNumber().get());
        //required가 들어가면 optional이 아니라 바로 .get()한 것 처럼 뽑아주는 듯(내용 없을 시 IllegalStateException throw)
        log.info("result.getMetadata().getRequiredRevisionNumber() >>" + result.getMetadata().getRequiredRevisionNumber()); //.get()과 결과 같음
        //optional로 리버전 타임스탬프 반환
        log.info("result.getMetadata().getRevisionInstant() >>" + result.getMetadata().getRevisionInstant());
        log.info("result.getMetadata().getRevisionInstant().get() >>" + result.getMetadata().getRevisionInstant().get());
        log.info("result.getMetadata().getRequiredRevisionInstant() >>" + result.getMetadata().getRequiredRevisionInstant());
        //UNKNOWN, INSERT, UPDATE, DELETE 중 리비전 타입 반환
        log.info("result.getMetadata().getRevisionType() >>" + result.getMetadata().getRevisionType());
        //----- metadata가 아니어도 바로 뽑아올 수 있다! -----//
        log.info("result.getRevisionNumber() >>"+result.getRevisionNumber());
        log.info("result.getRequiredRevisionNumber() >>"+result.getRequiredRevisionNumber());
        log.info("result.getRevisionInstant() >>"+result.getRevisionInstant());
        log.info("result.getRequiredRevisionInstant() >>"+result.getRequiredRevisionInstant());
      }
    );
  }

  @Test
  public void getFindLastChangeRevision(){
    //한 엔티티 객체의 여러 변경 내역이 있다면 가장 최신 정보 가져오기
    Optional<Revision<Integer, DiscountInfo>> lastChangeRevision = discountInfoRepository.findLastChangeRevision(2L);
    log.info("최신 내역 리비전 번호 >> {}",lastChangeRevision.get().getRequiredRevisionNumber());
    log.info("최신 내역 타임스탬프 >> {}", lastChangeRevision.get().getRequiredRevisionInstant());
    log.info("최신 내역 리비전 타입 >> {}", lastChangeRevision.get().getMetadata().getRevisionType());
  }

  @Test
  public void getFindRevisionsPage(){

    //한 엔티티 객체의 모든 내역 페이징
    //결과 -> 페이징은 되는데 정렬이 안 됨
    List<ProductInfo> resultList = productInfoService.pagingTestByAuditReader(2L);

    resultList.stream()
      .forEach(productInfo -> {
        log.info("productInfo.getId() >> {}", productInfo.getName());
        log.info("productInfo.getId() >> {}", productInfo.getModifiedDate());
      });
    // AuditOrder auditOrder = AuditOrder.OrderData;
    Sort.Direction direction = Sort.Direction.DESC;
    Sort sort= Sort.by(direction, "modifiedDate");
    Page<Revision<Integer, ProductInfo>> rev =
      productInfoRepository.findRevisions(2L, PageRequest.of(0, 2, sort));

    log.info("rev.getTotalElements() >> {}", rev.getTotalElements());
    log.info("rev.getTotalPages() >> {}", rev.getTotalPages());
    AtomicInteger orderCnt = new AtomicInteger(0);
    rev.getContent().stream()
        .forEach(result -> {
          orderCnt.getAndIncrement();
          log.info("상품 아이디 >> {}", result.getEntity().getId());
          log.info("{}번째 결과 리비전 넘버 >> {}", orderCnt, result.getRequiredRevisionNumber());
          log.info("{}번째 결과 리비전 타임스탬프 >> {}", orderCnt, result.getRequiredRevisionInstant());
        });
  }

  @Test
  public void getPageTest3(){
    List<DiscountInfo> result = auditReader.createQuery()
      .forRevisionsOfEntity(DiscountInfo.class, true, true)
      .add(AuditEntity.id().eq(2L))
      .add(AuditEntity.property("name").like("할인", MatchMode.ANYWHERE))
      .add(AuditEntity.revisionType().eq(RevisionType.MOD))
      .addOrder(AuditEntity.revisionNumber().desc())
      .getResultList();

    result.stream()
      .forEach(r -> {
        log.info("r.getModifiedBy() >>"+r.getModifiedBy());
        log.info("r.getName() >>"+r.getName());
      });
  }
}