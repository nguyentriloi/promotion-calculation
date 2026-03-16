package com.interceptor.order.domain.model.valueobjects;

public record Condition(

		String field,
		Operator operator,
		Object value

) {}
