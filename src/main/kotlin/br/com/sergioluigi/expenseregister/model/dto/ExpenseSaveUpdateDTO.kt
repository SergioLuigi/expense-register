package br.com.sergioluigi.expenseregister.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class ExpenseSaveUpdateDTO(

    @field:NotBlank
    @JsonProperty("description", required = true)
    val description: String,

    @field:NotBlank
    @field:Size(min = 3, max = 50)
    @JsonProperty("responsibleName", required = true)
    val responsibleName: String,

    @field:NotNull
    @JsonProperty("value", required = true)
    val value: BigDecimal,

    @JsonProperty("tagNames", required = true)
    val tagNames: MutableList<String> = mutableListOf()

)