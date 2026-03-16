package com.interceptor.order.domain.service;

import com.interceptor.order.application.dto.PromotionContext;
import com.interceptor.order.domain.model.order.Order;
import com.interceptor.order.domain.model.order.OrderItem;
import com.interceptor.order.domain.model.promotion.Adjustment;
import com.interceptor.order.domain.model.valueobjects.Action;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ActionExecutor {

  public List<Adjustment> execute(
		  Action action,
		  PromotionContext context
  ) {

	Order order = context.order();

	switch (action.type()) {

	  case ORDER_PERCENT_DISCOUNT -> {

		BigDecimal discount =
				order.totalPrice()
						.multiply(action.value());

		return List.of(
				new Adjustment(
						"PROMO",
						"Order percent discount",
						discount
				)
		);
	  }

	  case ORDER_FIXED_DISCOUNT -> {

		return List.of(
				new Adjustment(
						"PROMO",
						"Order fixed discount",
						action.value()
				)
		);
	  }

	  case ADD_FREE_ITEM -> {

		order.getItems().add(
				new OrderItem(
						action.sku(),
						1,
						BigDecimal.ZERO
				)
		);

		return List.of(
				new Adjustment(
						"PROMO",
						"Free product",
						BigDecimal.ZERO
				)
		);
	  }

	  default -> {
		return List.of();
	  }
	}

  }

}
