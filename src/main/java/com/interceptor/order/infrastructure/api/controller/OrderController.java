package com.interceptor.order.infrastructure.api.controller;

import com.interceptor.order.application.service.OrderService;
import com.interceptor.order.infrastructure.api.request.OrderRequest;
import com.interceptor.order.infrastructure.api.response.OrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping("calculate")
  public OrderResponse calculate(@RequestBody @Valid OrderRequest request) {
    return orderService.calculate(request);
  }
}
