package com.interceptor.order.domain.utils;

import com.interceptor.order.application.dto.PromotionContext;
import com.interceptor.order.domain.model.order.OrderItem;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.function.Function;

@UtilityClass
public class FieldResolver {

  private static final Map<String, Function<PromotionContext, Object>>
		  resolvers = Map.of(

		  "order.total",
		  ctx -> ctx.order().totalPrice(),

		  "account.type",
		  ctx -> ctx.account().type(),

		  "items.sku",
		  ctx -> ctx.items()
				  .stream()
				  .map(OrderItem::sku)
				  .toList()
  );

  public static Object resolve(
		  PromotionContext context,
		  String field
  ) {

	var resolver = resolvers.get(field);

	if (resolver == null)
	  throw new RuntimeException("Unknown field: " + field);

	return resolver.apply(context);
  }

}
