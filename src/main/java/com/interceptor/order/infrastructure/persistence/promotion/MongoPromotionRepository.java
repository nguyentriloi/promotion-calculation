package com.interceptor.order.infrastructure.persistence.promotion;

import com.interceptor.order.domain.model.valueobjects.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoPromotionRepository extends MongoRepository<PromotionEntity, String> {

  List<PromotionEntity> findAllByStatus(Status status);
}