package br.com.sergioluigi.expenseregister.model.dto

import br.com.sergioluigi.expenseregister.infra.database.entity.Expense
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class ExpenseDTO(

    @JsonProperty("id")
    val id: UUID?,

    @JsonProperty("description")
    val description: String,

    @JsonProperty("responsibleName", required = true)
    val responsibleName: String,

    @JsonProperty("value")
    val value: BigDecimal,

    @JsonProperty("tags")
    val tags: List<TagDTO> = emptyList(),

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val expenseDate: LocalDateTime
) {
    constructor(expense: Expense): this(
        id = expense.id,
        description = expense.description,
        responsibleName = expense.responsibleName,
        value = expense.value,
        tags = expense.tags.map { TagDTO(it) },
        expenseDate = expense.createdDate
    )

    override fun equals(other: Any?): Boolean =
        other is ExpenseDTO &&
        other.expenseDate == this.expenseDate &&
        other.description == this.description &&
        other.responsibleName == this.responsibleName &&
        other.value == this.value &&
        other.tags.size == this.tags.size &&
        other.tags.containsAll(this.tags)
}