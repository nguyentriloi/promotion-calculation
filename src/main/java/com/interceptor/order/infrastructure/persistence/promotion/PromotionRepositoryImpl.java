package com.interceptor.order.infrastructure.persistence.promotion;

import com.interceptor.order.domain.model.promotion.Promotion;
import com.interceptor.order.domain.model.valueobjects.Status;
import com.interceptor.order.domain.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionRepositoryImpl implements PromotionRepository {

  private final MongoPromotionRepository mongoPromotionRepository;
  @Override
  public List<Promotion> findActivePromotions() {
	return mongoPromotionRepository.findAllByStatus(Status.ACTIVE)
			.stream()
			.map(entity -> new Promotion(
					entity.priority(),
					entity.conditions(),
					entity.actions()
			)).collect(Collectors.toList());
  }
}
