package com.interceptor.order.domain.model.order;

import com.interceptor.order.domain.model.account.Account;
import com.interceptor.order.domain.model.promotion.Adjustment;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Data
public class Order {

  private final String id;
  private final List<OrderItem> items;
  private final Account account;
  private final String couponCode;
  private final List<Adjustment> adjustments = new ArrayList<>();

  public BigDecimal totalPrice() {
	return items.stream()
			.map(OrderItem::totalPrice)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  public void addAdjustment(Adjustment adjustment) {
	adjustments.add(adjustment);
  }

}
