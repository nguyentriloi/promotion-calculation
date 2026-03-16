package com.interceptor.order.domain.model.order;

import java.math.BigDecimal;

public record OrderItem(
        String sku,
        int quantity,
        BigDecimal price
) {

  public BigDecimal totalPrice() {
    return price.multiply(BigDecimal.valueOf(quantity));
  }
}