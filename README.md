# Promotion Engine (Rule-Based) – Clean Architecture

This project demonstrates a **rule-based promotion engine for e-commerce order calculation**, implemented using **Clean Architecture**.

The system supports dynamic promotions such as:

* **Coupon discounts**
* **VIP / account-based discounts**
* **Campaign promotions** (e.g. Buy 2 Get 1)
* **Rule-based promotion evaluation using a DSL**

The goal of this design is to achieve:

* **Extensibility** – easily add new promotion types
* **Separation of concerns** – clear domain boundaries
* **Scalability** – support many promotion rules without code changes

---

# 1. Architecture Overview

The project follows **Clean Architecture**, ensuring the business logic remains independent from frameworks and infrastructure.

```
API Layer (Infrastructure)
        │
        ▼
Application Layer
        │
        ▼
Domain Layer
        │
        ▼
Infrastructure Layer
```

### Responsibilities

**API Layer**

* Receives HTTP requests
* Converts requests into application use cases

**Application Layer**

* Coordinates use cases
* Orchestrates domain services and repositories

**Domain Layer**

* Contains core business rules
* Implements the promotion engine and rule evaluation logic

**Infrastructure Layer**

* Handles persistence and framework integrations
* Implements repository interfaces

---

# 2. Project Structure

```
src/main/java/com.interceptor.order

application
 ├── service
 │     ├── OrderService
 │     └── PromotionService
 │
 ├── dto
 │     └── PromotionContext
 │
 └── mapper
       └── OrderMapper

domain
 ├── model
 │     ├── Order
 │     ├── OrderItem
 │     ├── Account
 │
 │     └── promotion
 │           ├── Promotion
 │           ├── Condition
 │           ├── Action
 │           ├── Adjustment
 │
 ├── service
 │     ├── PromotionEngine
 │     ├── ConditionEvaluator
 │     └── ActionExecutor
 │
 ├── util
 │     └── FieldResolver
 │
 └── repository
       ├── PromotionRepository
       └── CouponRepository

infrastructure
 ├── persistence
 │     ├── MongoPromotionRepository
 │     └── MongoCouponRepository
 │
 └── api
       └── controller
            └── OrderController
```

---

# 3. Core Domain Concepts

## Order

Represents a customer's order and the applied promotion results.

Responsibilities:

* Calculate **total price**
* Store **promotion adjustments**
* Compute **final discounted price**

---

# 4. Promotion Rule Engine

The **PromotionEngine** dynamically evaluates promotion rules.

Execution flow:

```
Order
   │
   ▼
PromotionService
   │
   ▼
Load promotions from repositories
   │
   ▼
PromotionEngine.evaluate()
   │
   ▼
ConditionEvaluator
   │
   ▼
ActionExecutor
   │
   ▼
Adjustments applied to Order
```

### Engine Responsibilities

* Evaluate promotion conditions
* Execute promotion actions
* Return calculated adjustments

Promotions are evaluated based on **priority order**.

---

# 5. Promotion DSL

Promotions are stored in the database using a simple DSL format.

Example:

```
{
  "id": "VIP5",
  "priority": 5,
  "conditions": [
    {
      "field": "account.type",
      "operator": "EQUAL",
      "value": "VIP"
    }
  ],
  "actions": [
    {
      "type": "ORDER_PERCENT_DISCOUNT",
      "value": 0.05
    }
  ]
},
{
  "id": "BUY2GET1",
  "priority": 5,
  "conditions": [
    {
      "field": "items.sku",
      "operator": "CONTAINS",
      "value": "COCA"
    },
    {
      "field": "items.quantity",
      "operator": "GREATER_THAN",
      "value": 2
    }
  ],
  "actions": [
    {
      "type": "ADD_FREE_ITEM",
      "sku": "COCA",
      "value": 1
    }
  ]
}
```

This approach allows business teams to **configure promotions without modifying application code**.

---

# 6. Coupon Support

Coupons are stored separately from campaign promotions.

Example coupon:

```
{
  "code": "SUMMER10",
  "discount": 10,
  "minOrderValue": 50
}
```

Checkout flow:

1. Client sends a `couponCode`
2. System loads the coupon from the database
3. The coupon is converted into a promotion rule
4. The promotion engine evaluates it together with other promotions

---

# 7. API Example

### Request

```
POST /api/v1/orders/calculation
```

```
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
```

---

# 8. Promotion Calculation

Order details:

```
Item price = 100
Quantity = 2
Order total = 200
```

Applicable promotions:

**VIP Promotion**

```
5% discount
200 × 5% = 10
```

**Coupon SUMMER10**

```
Fixed discount = 10
(min order value = 50 → valid)
```

**Buy 2 Get 1**

```
Condition: quantity > 2
Current quantity = 2
→ Not triggered
```

---

# 9. Response

Final calculation:

```
Order total:      200
VIP discount:     10
Coupon discount:  10
---------------------
Total discount:   20
Final price:      180
```

### Response

```
{
  "orderTotal": 200,
  "discount": 20,
  "finalPrice": 180
}
```

---
