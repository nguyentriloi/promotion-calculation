package com.interceptor.order.domain.service;

import com.interceptor.order.application.dto.PromotionContext;
import com.interceptor.order.domain.model.promotion.Adjustment;
import com.interceptor.order.domain.model.promotion.Promotion;
import com.interceptor.order.domain.model.valueobjects.Action;
import com.interceptor.order.domain.model.valueobjects.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PromotionEngineTest {

  private ConditionEvaluator evaluator;
  private ActionExecutor executor;
  private PromotionEngine engine;

  @BeforeEach
  void setup() {
	evaluator = mock(ConditionEvaluator.class);
	executor = mock(ActionExecutor.class);

	engine = new PromotionEngine(evaluator, executor);
  }

  @Test
  void shouldApplyPromotionWhenConditionsMatch() {

	PromotionContext context = mock(PromotionContext.class);

	Condition condition = mock(Condition.class);
	Action action = mock(Action.class);

	Promotion promotion = mock(Promotion.class);

	when(promotion.priority()).thenReturn(1);
	when(promotion.conditions()).thenReturn(List.of(condition));
	when(promotion.actions()).thenReturn(List.of(action));

	when(evaluator.evaluate(condition, context)).thenReturn(true);

	Adjustment adjustment =
			new Adjustment("ORDER", "DISCOUNT", new BigDecimal("10"));

	when(executor.execute(action, context))
			.thenReturn(List.of(adjustment));

	List<Adjustment> result =
			engine.evaluate(context, List.of(promotion));

	assertThat(result).hasSize(1);
	assertThat(result.get(0).amount()).isEqualByComparingTo("10");

	verify(evaluator).evaluate(condition, context);
	verify(executor).execute(action, context);
  }

  @Test
  void shouldSkipPromotionWhenConditionFails() {

	PromotionContext context = mock(PromotionContext.class);

	Condition condition = mock(Condition.class);
	Action action = mock(Action.class);

	Promotion promotion = mock(Promotion.class);

	when(promotion.priority()).thenReturn(1);
	when(promotion.conditions()).thenReturn(List.of(condition));
	when(promotion.actions()).thenReturn(List.of(action));

	when(evaluator.evaluate(condition, context)).thenReturn(false);

	List<Adjustment> result =
			engine.evaluate(context, List.of(promotion));

	assertThat(result).isEmpty();

	verify(executor, never()).execute(any(), any());
  }

  @Test
  void shouldExecutePromotionsInPriorityOrder() {

	PromotionContext context = mock(PromotionContext.class);

	Condition condition = mock(Condition.class);
	Action action = mock(Action.class);

	Promotion promo1 = mock(Promotion.class);
	Promotion promo2 = mock(Promotion.class);

	when(promo1.priority()).thenReturn(2);
	when(promo2.priority()).thenReturn(1);

	when(promo1.conditions()).thenReturn(List.of(condition));
	when(promo2.conditions()).thenReturn(List.of(condition));

	when(promo1.actions()).thenReturn(List.of(action));
	when(promo2.actions()).thenReturn(List.of(action));

	when(evaluator.evaluate(any(), any())).thenReturn(true);

	when(executor.execute(any(), any()))
			.thenReturn(List.of(new Adjustment("ORDER", "DISCOUNT", BigDecimal.ONE)));

	engine.evaluate(context, List.of(promo1, promo2));

	verify(executor, times(2)).execute(any(), any());
  }

}