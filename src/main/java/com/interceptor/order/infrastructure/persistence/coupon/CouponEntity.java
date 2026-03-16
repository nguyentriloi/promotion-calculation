package com.interceptor.order.infrastructure.persistence.coupon;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document("coupon")
public record CouponEntity(

		@Id
		String id,

		String code,

		BigDecimal discount,

		BigDecimal minOrderValue,

		LocalDateTime expiredAt
) {}
