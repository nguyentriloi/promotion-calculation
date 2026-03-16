package com.interceptor.order.infrastructure.api.request;

import com.interceptor.order.domain.model.valueobjects.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequest(
		@NotBlank
		String accountId,
		@NotNull
		AccountType accountType,
		String couponCode,
		@NotEmpty
		List<ItemRequest> items
) {

  public record ItemRequest(
		  String sku,
		  int quantity,
		  BigDecimal price
  ) {
  }
}
