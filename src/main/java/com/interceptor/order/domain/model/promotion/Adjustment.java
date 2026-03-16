package com.interceptor.order.domain.model.promotion;

import java.math.BigDecimal;

public record Adjustment(
		String promotionId,
		String description,
		BigDecimal amount
) {}
