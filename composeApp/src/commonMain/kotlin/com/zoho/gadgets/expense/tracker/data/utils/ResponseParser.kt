package com.zoho.gadgets.expense.tracker.data.utils

import com.zoho.gadgets.expense.tracker.domain.enums.PurchaseRecommendationHeaderType
import com.zoho.gadgets.expense.tracker.domain.models.DebtRecommendationResponse
import com.zoho.gadgets.expense.tracker.domain.models.PurchaseRecommendationResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class ResponseParser {
    fun parsePurchaseRecommendation(
        response: String,
    ): List<PurchaseRecommendationResponse> {
        val list = mutableListOf<PurchaseRecommendationResponse>()
        runCatching {
            val jsonObject = Json.decodeFromString<JsonElement>(response).jsonObject
            jsonObject.keys.mapNotNull { jsonKey ->
                val key = PurchaseRecommendationHeaderType.entries
                    .find { it.key == jsonKey } ?: return@mapNotNull null

                val keyObject = jsonObject[key.key]?.jsonObject
                val reason = keyObject?.get("reason")?.jsonPrimitive?.content
                val recommendation = keyObject?.get("recommendation")?.jsonPrimitive?.content

                PurchaseRecommendationResponse(
                    key = key,
                    reason = reason.orEmpty(),
                    recommendation = recommendation.orEmpty()
                )
            }.let { list.addAll(it) }
        }.onFailure { e ->
            e.printStackTrace()
        }
        return list
    }

    fun parseDebtRecommendation(
        response: String,
    ): List<DebtRecommendationResponse> {
        val list = mutableListOf<DebtRecommendationResponse>()
        runCatching {
            val jsonArray = Json.decodeFromString<JsonElement>(response).jsonArray
            jsonArray.mapNotNull { jsonElement ->
                val suggestionJsonObject = jsonElement.jsonObject["suggestion"]?.jsonObject
                    ?: return@mapNotNull null

                val description = suggestionJsonObject["description"]?.jsonPrimitive?.content
                val action = suggestionJsonObject["action"]?.jsonPrimitive?.content

                DebtRecommendationResponse(
                    description = description.orEmpty(),
                    action = action.orEmpty()
                )
            }.let { list.addAll(it) }
        }.onFailure { e ->
            e.printStackTrace()
        }
        return list
    }
}