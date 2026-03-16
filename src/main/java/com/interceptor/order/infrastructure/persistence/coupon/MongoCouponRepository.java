package com.interceptor.order.infrastructure.persistence.coupon;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MongoCouponRepository extends MongoRepository<CouponEntity, String> {

  Optional<CouponEntity> findByCodeAndExpiredAtGreaterThan(String code, LocalDateTime now);
}