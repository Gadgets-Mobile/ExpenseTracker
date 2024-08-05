package viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.DebtResponse
import data.Response
import data.enums.Type
import data.getReponseParser
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.content
import dev.shreyaspatil.ai.client.generativeai.type.generationConfig
import domain.repo.AmountRepository
import domain.repo.getAmountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecommendationViewmodel(
    private val repository: AmountRepository = getAmountRepository(),
) : ViewModel() {

    private val apiKey = "AIzaSyABXyNFmn5KjvaYqpS0B_jATAdEBKN6kGA"

    private val _expense = MutableStateFlow(0)
    val expense = _expense.asStateFlow()

    private val _recommendation = MutableStateFlow<List<Response>>(emptyList())
    val recommendation = _recommendation.asStateFlow()

    private val _debtRecommendation = MutableStateFlow<List<DebtResponse>>(emptyList())
    val debtRecommendation = _debtRecommendation.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun calculateTotalExpense() {
        _expense.update {
            repository.getAllEntries()
                .filter { it.type == Type.EXPENSE }
                .sumOf { it.amount }
        }
    }

    fun generateRecommendation(
        income: Int,
        productValue: Int,
        totalExpenses: Int,
        isEmiMandatory: Boolean,
    ) {
        viewModelScope.launch {
            _recommendation.update { emptyList() }
            _loading.update { true }
            val model = GenerativeModel(
                "gemini-1.5-flash",
                apiKey,
                generationConfig = generationConfig {
                    temperature = 0f
                    topK = 64
                    topP = 0.95f
                    maxOutputTokens = 8192
                },
                systemInstruction = content { text("Give this below details in JSON format only\n\n    when can I buy so that not affect my current lifestyle - json key is purchase_timeline\n\n    should opt for emi - json key should be emi_decision\n\n    where to save for this if emi - json key should be emi_savings\n\n    how to proceed based on if it is need/want - json key should be need_vs_want_action\n\nif emi_decision is true then suggest the best emi plan and how cloe the emi quickly, consider inflation value \n\nthere should be common key and value inside al thses object that should have recommendation ( this field should suggest excat month)and reason. The reason should be concise and should have exact value") },
            )

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

            val responseParser = getReponseParser()
            val parsedResponse = responseJson?.let(responseParser::parse)

            _recommendation.update { parsedResponse ?: emptyList() }
            _loading.update { false }
        }
    }

    fun generateRecommendationForDebt(
        monthlyEMI: String,
        totalLoan: String,
        salary: String,
        paidPeriod: String,
        totalPaymentPeriod: String,
        interestRate: String,
        expenses: Int,
    ) {
        viewModelScope.launch {
            _recommendation.update { emptyList() }
            _loading.update { true }
            val model = GenerativeModel(
                "gemini-1.5-flash",
                apiKey,
                generationConfig = generationConfig {
                    temperature = 0f
                    topK = 64
                    topP = 0.95f
                    maxOutputTokens = 8192
                },
                systemInstruction = content {
                    text(
                        "Give the below details in JSON format only\nuser will give input of total debt amount, interest rate, total months or years, paid month or years, monthly salary (optional)\nif the interest rate is not specified consider 8.5 percentage for HOME LOAN\n14 % for Personla Loan\n12 percentage for VEHICLE LOAN\nsuggest the user on how to quickly repay the debt with best possible options\nsuggest a quick repayment plan\nthe suggestion object must contain only description and action key(repaying action in amount )\nhow much can i increase my repayment monthly \nsuggest exact amount for  increasing monthly payment based on the calculation\nif there is more than 1 bank account show the JSON in a array format.\nSimplify Language: Use clear and concise language that is easy for everyone to understand. Avoid technical jargon.\nProvide clear steps the user can take to implement the suggestions, such as:  \n\"Contact your lender to discuss a lower interest rate.\"\n\"Set up automatic payments to avoid late fees.\"\n\"Use a budgeting app to track your expenses.\"\n"
                    )
                },
            )

            val chat = model.startChat()
            val response = chat.sendMessage(
                """
               Monthly Emi : $monthlyEMI
               Total Loan Amount : $totalLoan
               Monthly Expense : $expenses
               Month Paid: $paidPeriod
               Total period: $totalPaymentPeriod
               Interest : $interestRate%
               Monthly Salary : $salary
           """.trimIndent()
            )

            val responseJson = response.text
                ?.replace("```json", "")
                ?.replace("```", "")
                ?.trim()

            val responseParser = getReponseParser()
            val parsedResponse = responseJson?.let(responseParser::parseDebt)

            _debtRecommendation.update { parsedResponse ?: emptyList() }
            _loading.update { false }
        }
    }

    fun clearRecommendation() {
        _recommendation.update { emptyList() }
        _debtRecommendation.update { emptyList() }
        _loading.update { false }
    }

}