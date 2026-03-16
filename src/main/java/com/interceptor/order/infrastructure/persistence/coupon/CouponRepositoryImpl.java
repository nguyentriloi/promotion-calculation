package com.interceptor.order.infrastructure.persistence.coupon;

import com.interceptor.order.domain.model.promotion.Coupon;
import com.interceptor.order.domain.model.promotion.Promotion;
import com.interceptor.order.domain.model.valueobjects.Status;
import com.interceptor.order.domain.repository.CouponRepository;
import com.interceptor.order.domain.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

  private final MongoCouponRepository mongoCouponRepository;
  @Override
  public Optional<Coupon> findByCouponCode(String code) {
	return mongoCouponRepository.findByCodeAndExpiredAtGreaterThan(code, LocalDateTime.now())
			.map(entity -> new Coupon(entity.code(), entity.discount(), entity.minOrderValue()));
  }
}
