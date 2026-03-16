package com.interceptor.order.domain.service;

import com.interceptor.order.application.dto.PromotionContext;
import com.interceptor.order.domain.model.promotion.Adjustment;
import com.interceptor.order.domain.model.promotion.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionEngine {

  private final ConditionEvaluator evaluator;
  private final ActionExecutor executor;

  public List<Adjustment> evaluate(

		  PromotionContext context,
		  List<Promotion> promotions

  ) {

	List<Adjustment> adjustments = new ArrayList<>();

	promotions.stream()
			.sorted(Comparator.comparing(Promotion::priority))
			.forEach(promotion -> {

			  boolean matched =
					  promotion.conditions()
							  .stream()
							  .allMatch(c ->
									  evaluator.evaluate(c, context)
							  );

			  if (matched) {

				promotion.actions()
						.forEach(action ->
								adjustments.addAll(
										executor.execute(action, context)
								));
			  }

			});

	return adjustments;

  }

}
