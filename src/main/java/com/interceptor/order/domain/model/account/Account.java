package com.interceptor.order.domain.model.account;

import com.interceptor.order.domain.model.valueobjects.AccountType;

public record Account(
		String id,
		AccountType type
) {
}
