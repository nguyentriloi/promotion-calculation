package com.interceptor.order.domain.model.valueobjects;

import java.math.BigDecimal;

public record Discount(
		DiscountType type,
		BigDecimal value
) {

  public BigDecimal calculate(BigDecimal amount) {
	return switch (type) {
	  case PERCENTAGE -> amount.multiply(value);
	  case FIXED -> value;
	};
  }
}