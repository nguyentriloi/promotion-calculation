package com.interceptor.order.application.mapper;

import com.interceptor.order.domain.model.account.Account;
import com.interceptor.order.domain.model.order.Order;
import com.interceptor.order.domain.model.order.OrderItem;
import com.interceptor.order.domain.model.promotion.Adjustment;
import com.interceptor.order.infrastructure.api.request.OrderRequest;
import com.interceptor.order.infrastructure.api.response.OrderResponse;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class OrderMapper {

  public Order mapToOrder(OrderRequest request) {
	List<OrderItem> items = request.items()
			.stream()
			.map(i -> new OrderItem(i.sku(), i.quantity(), i.price()))
			.toList();

	Account account = new Account(request.accountId(), request.accountType());

	return new Order(UUID.randomUUID().toString(), items, account, request.couponCode());
  }

  public OrderResponse mapToOrderResponse(List<Adjustment> adjustments, Order order) {
	BigDecimal discount = adjustments.stream()
			.map(Adjustment::amount)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
	BigDecimal subTotal = order.totalPrice();
	BigDecimal finalPrice = subTotal.subtract(discount);

	return new OrderResponse(subTotal, discount, finalPrice);
  }
}
