package data

enum class ResponseKey(val key: String) {
    PurchaseTimeline("purchase_timeline"),
    EMIDecision("emi_decision"),
    EMISavings("emi_savings"),
    NeedVsWantAction("need_vs_want_action")
}

data class Response(
    val key: ResponseKey,
    val reason: String,
    val recommendation: String,
)

data class DebtResponse(
    val description: String,
    val action: String,
)

interface ResponseParser {
    fun parse(response: String): List<Response>
    fun parseDebt(response: String): List<DebtResponse>
}

expect fun getReponseParser(): ResponseParser