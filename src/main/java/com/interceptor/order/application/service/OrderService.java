package com.interceptor.order.application.service;

import com.interceptor.order.application.mapper.OrderMapper;
import com.interceptor.order.domain.model.order.Order;
import com.interceptor.order.domain.model.promotion.Adjustment;
import com.interceptor.order.infrastructure.api.request.OrderRequest;
import com.interceptor.order.infrastructure.api.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final PromotionService promotionService;

  public OrderResponse calculate(OrderRequest request) {
	Order order = OrderMapper.mapToOrder(request);

	List<Adjustment> adjustments = promotionService.applyPromotions(order);

	return OrderMapper.mapToOrderResponse(adjustments, order);
  }
}
