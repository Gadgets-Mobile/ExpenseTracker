package com.zoho.gadgets.expense.tracker.data.repository

import com.zoho.gadgets.expense.tracker.data.utils.ResponseParser
import com.zoho.gadgets.expense.tracker.domain.models.DebtRecommendationResponse
import com.zoho.gadgets.expense.tracker.domain.models.PurchaseRecommendationResponse
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.content
import dev.shreyaspatil.ai.client.generativeai.type.generationConfig

class GenerativeAiRepository(
    private val responseParser: ResponseParser,
) {
    private val modelName = "gemini-1.5-flash"
    private val apiKey = "YOUR_API_KEY"
    private val generationConfig = generationConfig {
        temperature = 0f
        topK = 64
        topP = 0.95f
        maxOutputTokens = 8192
    }

    suspend fun getPurchaseRecommendations(
        income: Int,
        productValue: Int,
        totalExpenses: Int,
        isEmiMandatory: Boolean,
    ): List<PurchaseRecommendationResponse> {
        val model = getPurchaseRecommendationModel()
        val chat = model.startChat()
        val response = chat.sendMessage(
            """
               income = $income
               expense = $totalExpenses
               product = $productValue
               emi required = $isEmiMandatory
           """.trimIndent()
        )

        val responseJson = response.text
            ?.replace("```json", "")
            ?.replace("```", "")
            ?.trim()

        val parsedResponse = responseJson?.let(responseParser::parsePurchaseRecommendation)
        return parsedResponse ?: emptyList()
    }

    private fun getPurchaseRecommendationModel(): GenerativeModel {
        return GenerativeModel(
            modelName = modelName,
            apiKey = apiKey,
            generationConfig = generationConfig,
            systemInstruction = content {
                text("Give this below details in JSON format only\n\n    when can I buy so that not affect my current lifestyle - json key is purchase_timeline\n\n    should opt for emi - json key should be emi_decision\n\n    where to save for this if emi - json key should be emi_savings\n\n    how to proceed based on if it is need/want - json key should be need_vs_want_action\n\nif emi_decision is true then suggest the best emi plan and how cloe the emi quickly, consider inflation value \n\nthere should be common key and value inside al thses object that should have recommendation ( this field should suggest excat month)and reason. The reason should be concise and should have exact value")
            },
        )
    }

    suspend fun getDebtRecommendations(
        monthlyEMI: String,
        totalLoan: String,
        salary: String,
        paidPeriod: String,
        totalPaymentPeriod: String,
        interestRate: String,
        expenses: Int,
    ): List<DebtRecommendationResponse> {
        val model = getDebtRecommendationModel()
        val chat = model.startChat()
        val response = chat.sendMessage(
            """
               Monthly Emi : $monthlyEMI
               Total Loan Amount : $totalLoan
               Monthly Expense : $expenses
               Month Paid : $paidPeriod
               Total period : $totalPaymentPeriod
               Interest : $interestRate%
               Monthly Salary : $salary
           """.trimIndent()
        )

        val responseJson = response.text
            ?.replace("```json", "")
            ?.replace("```", "")
            ?.trim()

        val parsedResponse = responseJson?.let(responseParser::parseDebtRecommendation)
        return parsedResponse ?: emptyList()
    }

    private fun getDebtRecommendationModel(): GenerativeModel {
        return GenerativeModel(
            modelName = modelName,
            apiKey = apiKey,
            generationConfig = generationConfig,
            systemInstruction = content {
                text(
                    "Give the below details in JSON format only\nuser will give input of total debt amount, interest rate, total months or years, paid month or years, monthly salary (optional)\nif the interest rate is not specified consider 8.5 percentage for HOME LOAN\n14 % for Personla Loan\n12 percentage for VEHICLE LOAN\nsuggest the user on how to quickly repay the debt with best possible options\nsuggest a quick repayment plan\nthe suggestion object must contain only description and action key(repaying action in amount )\nhow much can i increase my repayment monthly \nsuggest exact amount for  increasing monthly payment based on the calculation\nif there is more than 1 bank account show the JSON in a array format.\nSimplify Language: Use clear and concise language that is easy for everyone to understand. Avoid technical jargon.\nProvide clear steps the user can take to implement the suggestions, such as:  \n\"Contact your lender to discuss a lower interest rate.\"\n\"Set up automatic payments to avoid late fees.\"\n\"Use a budgeting app to track your expenses.\"\n"
                )
            },
        )
    }
}