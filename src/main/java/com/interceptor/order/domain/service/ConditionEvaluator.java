package com.interceptor.order.domain.service;

import com.interceptor.order.application.dto.PromotionContext;
import com.interceptor.order.domain.model.valueobjects.Condition;
import com.interceptor.order.domain.utils.FieldResolver;
import org.springframework.stereotype.Service;

@Service
public class ConditionEvaluator {

  public boolean evaluate(
		  Condition condition,
		  PromotionContext context
  ) {

	Object actualValue =
			FieldResolver.resolve(
					context,
					condition.field()
			);

	return switch (condition.operator()) {

	  case EQUAL -> actualValue.toString().equals(condition.value().toString());

	  case GREATER_THAN -> ((Comparable) actualValue).compareTo(condition.value()) > 0;

	  case LESS_THAN -> ((Comparable) actualValue).compareTo(condition.value()) < 0;

	  default -> false;
	};
  }
}
