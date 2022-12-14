package br.com.sergioluigi.expenseregister.model.dto

import br.com.sergioluigi.expenseregister.infra.database.entity.Bill
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class BillDTO(

    @JsonProperty("code")
    val code: UUID,

    @JsonProperty("value")
    val value: BigDecimal,

    @JsonProperty("expireAt")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer::class)
    val expireAt: LocalDate
){
    constructor(bill: Bill): this(
        bill.code,
        bill.value,
        bill.expireAt
    )

    override fun equals(other: Any?): Boolean =
        other is BillDTO && code == other.code && value == other.value && expireAt == other.expireAt
}
