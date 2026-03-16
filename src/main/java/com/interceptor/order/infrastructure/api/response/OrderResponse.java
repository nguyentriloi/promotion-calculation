package com.interceptor.order.infrastructure.api.response;

import java.math.BigDecimal;

public record OrderResponse(
		BigDecimal subTotal,
		BigDecimal discount,
		BigDecimal finalPrice
) {
}
