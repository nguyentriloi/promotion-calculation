package com.interceptor.order.application.dto;

import com.interceptor.order.domain.model.account.Account;
import com.interceptor.order.domain.model.order.Order;
import com.interceptor.order.domain.model.order.OrderItem;

import java.util.List;

public record PromotionContext(

		Order order,
		Account account,
		List<OrderItem> items

) {}
