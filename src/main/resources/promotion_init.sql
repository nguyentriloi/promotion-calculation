[
  {
    "_id": "VIP5",
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
    ],
    "status": "ACTIVE"
  },
  {
    "_id": "BUY2GET1",
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
    ],
    "status": "ACTIVE"
  }
]