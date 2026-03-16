package com.interceptor.order.domain.model.valueobjects;

import java.math.BigDecimal;

public record Action(

		ActionType type,
		BigDecimal value,
		String sku

) {}
