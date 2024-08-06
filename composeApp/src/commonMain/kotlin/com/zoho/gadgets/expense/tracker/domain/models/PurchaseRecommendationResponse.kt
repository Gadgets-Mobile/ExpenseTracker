package com.zoho.gadgets.expense.tracker.domain.models

import com.zoho.gadgets.expense.tracker.domain.enums.PurchaseRecommendationHeaderType

data class PurchaseRecommendationResponse(
    val key: PurchaseRecommendationHeaderType,
    val reason: String,
    val recommendation: String,
)