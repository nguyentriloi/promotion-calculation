package com.interceptor.order.domain.model.promotion;

import com.interceptor.order.domain.model.valueobjects.*;

import java.math.BigDecimal;
import java.util.List;

public record Coupon(
		String code,
		BigDecimal discount,
		BigDecimal minOrderValue
) {

  public Promotion toPromotion() {
	return new Promotion(
			1,
			List.of(
					new Condition(
							"order.total",
							Operator.GREATER_THAN,
							minOrderValue
					)
			),
			List.of(
					new Action(
							ActionType.ORDER_FIXED_DISCOUNT,
							discount,
							null
					)
			)
	);
  }
}
