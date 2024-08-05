package data

import org.json.JSONArray
import org.json.JSONObject

object AndroidResponseParser : ResponseParser {

    override fun parse(response: String): List<Response> {
        val list = mutableListOf<Response>()
        runCatching {
            JSONObject(response).let { jsonObject ->
                jsonObject.keys().forEach { jsonKey ->
                    val key = ResponseKey.entries.find { it.key == jsonKey } ?: return@forEach
                    val reason = jsonObject.getJSONObject(key.key).getString("reason")
                    val recommendation =
                        jsonObject.getJSONObject(key.key).getString("recommendation")
                    list.add(Response(key, reason, recommendation))
                }
            }
        }.onFailure {
            it.printStackTrace()
        }
        return list
    }

    override fun parseDebt(response: String): List<DebtResponse> {
        val list = mutableListOf<DebtResponse>()
        runCatching {
            JSONArray(response).length().let { length ->
                for (i in 0 until length) {
                    val jsonObject =
                        JSONArray(response).getJSONObject(i).getJSONObject("suggestion")
                    val description = jsonObject.getString("description")
                    val action = jsonObject.getString("action")
                    list.add(DebtResponse(description, action))
                }
            }
        }.onFailure {
            it.printStackTrace()
        }
        return list
    }

}

actual fun getReponseParser(): ResponseParser = AndroidResponseParser