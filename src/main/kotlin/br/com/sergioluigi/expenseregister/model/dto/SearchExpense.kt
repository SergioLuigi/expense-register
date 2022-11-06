package br.com.sergioluigi.expenseregister.model.dto

import br.com.sergioluigi.expenseregister.model.util.annotation.ValidatePeriod
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.Period

@ValidatePeriod
class SearchExpense(

    @JsonProperty("term")
    val term: String?,//value or description

    @JsonProperty("startDate")
    val startDate: LocalDate?,

    @JsonProperty("finalDate")
    val finalDate: LocalDate?,

){
    fun checkPeriod(): Boolean =
        (startDate != null && finalDate != null && Period.between(startDate, finalDate).days >= 0) ||
                startDate == null || finalDate == null

}