package com.interceptor.order.application.service;

import com.interceptor.order.application.dto.PromotionContext;
import com.interceptor.order.domain.model.order.Order;
import com.interceptor.order.domain.model.promotion.Adjustment;
import com.interceptor.order.domain.model.promotion.Coupon;
import com.interceptor.order.domain.model.promotion.Promotion;
import com.interceptor.order.domain.repository.CouponRepository;
import com.interceptor.order.domain.repository.PromotionRepository;
import com.interceptor.order.domain.service.PromotionEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromotionService {

  private final PromotionRepository promotionRepository;
  private final CouponRepository couponRepository;

  private final PromotionEngine promotionEngine;

  public List<Adjustment> applyPromotions(Order order) {

	PromotionContext context =
			new PromotionContext(
					order,
					order.getAccount(),
					order.getItems()
			);

	List<Promotion> promotions = promotionRepository.findActivePromotions();
	Optional.ofNullable(order.getCouponCode())
			.flatMap(coupon -> couponRepository.findByCouponCode(coupon)
					.map(Coupon::toPromotion))
			.ifPresent(promotions::add);

	List<Adjustment> adjustments = promotionEngine.evaluate(context, promotions);

	adjustments.forEach(order::addAdjustment);

	return adjustments;
  }
}
