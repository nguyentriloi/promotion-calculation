package com.interceptor.order.infrastructure.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interceptor.order.application.service.OrderService;
import com.interceptor.order.infrastructure.api.request.OrderRequest;
import com.interceptor.order.infrastructure.api.response.OrderResponse;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Testcontainers
@SpringBootTest
public class OrderControllerIT {

    @Container
    @ServiceConnection
    static MongoDBContainer mongo =
            new MongoDBContainer("mongo:7.0");

    @Autowired
    OrderService orderService;

    RestTestClient client;

    @Autowired
    MongoTemplate mongoTemplate;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setupData() {
        client = RestTestClient.bindToController(new OrderController(orderService))
                        .baseUrl("/api/v1/orders")
                        .build();
        mongoTemplate.dropCollection("coupon");
        mongoTemplate.dropCollection("promotion");

        // coupon collection
        Document coupon = new Document()
                .append("code", "SUMMER10")
                .append("discount", 10)
                .append("minOrderValue", 50)
                .append("expiredAt", LocalDateTime.parse("2026-12-31T23:59:22"))
                .append("active", true);

        mongoTemplate.getCollection("coupon").insertOne(coupon);

        // promotion collection
        Document vipPromotion = new Document()
                .append("_id", "VIP5")
                .append("priority", 5)
                .append("conditions", List.of(
                        new Document("field", "account.type")
                                .append("operator", "EQUAL")
                                .append("value", "VIP")
                ))
                .append("actions", List.of(
                        new Document("type", "ORDER_PERCENT_DISCOUNT")
                                .append("value", 0.05)
                ))
                .append("status", "ACTIVE");

        Document buy2get1 = new Document()
                .append("_id", "BUY2GET1")
                .append("priority", 5)
                .append("conditions", List.of(
                        new Document("field", "items.sku")
                                .append("operator", "CONTAINS")
                                .append("value", "COCA"),
                        new Document("field", "items.quantity")
                                .append("operator", "GREATER_THAN")
                                .append("value", 2)
                ))
                .append("actions", List.of(
                        new Document("type", "ADD_FREE_ITEM")
                                .append("sku", "COCA")
                                .append("value", 1)
                ))
                .append("status", "ACTIVE");

        mongoTemplate.getCollection("promotion")
                .insertMany(List.of(vipPromotion, buy2get1));
    }

    @Test
    void shouldCalculateOrderWithVipAndCoupon() throws Exception {

        String request = """
                {
                  "accountId": "user1",
                  "accountType": "VIP",
                  "couponCode": "SUMMER10",
                  "items": [
                    {
                      "sku": "COCA",
                      "quantity": 2,
                      "price": 100
                    }
                  ]
                }
                """;

        client.post().uri("/calculate")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.readValue(request, OrderRequest.class))
                .exchange()
                .expectAll(
                        spec -> spec.expectStatus().isOk(),
                        spec -> spec.expectBody(OrderResponse.class)
                                .consumeWith(result -> {
                                    assert Objects.requireNonNull(result.getResponseBody()).subTotal().compareTo(BigDecimal.valueOf(200)) == 0;
                                    assert Objects.requireNonNull(result.getResponseBody()).discount().compareTo(BigDecimal.valueOf(20)) == 0;
                                    assert Objects.requireNonNull(result.getResponseBody()).finalPrice().compareTo(BigDecimal.valueOf(180)) == 0;
                                })
                );
    }
}
