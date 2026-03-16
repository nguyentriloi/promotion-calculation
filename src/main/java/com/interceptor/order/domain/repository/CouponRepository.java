package com.interceptor.order.domain.repository;

import com.interceptor.order.domain.model.promotion.Coupon;

import java.util.Optional;

public interface CouponRepository {
	Optional<Coupon> findByCouponCode(String code);
}
