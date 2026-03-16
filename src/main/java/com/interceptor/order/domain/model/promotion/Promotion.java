package com.interceptor.order.domain.model.promotion;

import com.interceptor.order.domain.model.valueobjects.Action;
import com.interceptor.order.domain.model.valueobjects.Condition;

import java.util.List;

public record Promotion(

		int priority,

		List<Condition> conditions,

		List<Action> actions
) {}
