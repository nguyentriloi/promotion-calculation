package com.interceptor.order.infrastructure.persistence.promotion;

import com.interceptor.order.domain.model.valueobjects.Action;
import com.interceptor.order.domain.model.valueobjects.Condition;
import com.interceptor.order.domain.model.valueobjects.Status;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("promotion")
public record PromotionEntity(

		@Id
		String id,

		int priority,

		List<Condition> conditions,

		List<Action> actions,
		Status status
) {}
