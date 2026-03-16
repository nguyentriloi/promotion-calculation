package com.interceptor.order.domain.repository;

import com.interceptor.order.domain.model.promotion.Promotion;

import java.util.List;

public interface PromotionRepository {
	List<Promotion> findActivePromotions();
}
