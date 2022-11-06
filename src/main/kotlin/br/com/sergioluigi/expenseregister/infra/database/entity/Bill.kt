package br.com.sergioluigi.expenseregister.infra.database.entity

import br.com.sergioluigi.expenseregister.model.dto.BillDTO
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "bill")
class Bill(

    @Id
    @GeneratedValue
    val id: UUID?,

    @Column(name = "code", unique = true)
    val code: UUID,

    @Column(name = "value")
    val value: BigDecimal,

    @Column(name = "expire_at")
    val expireAt: LocalDate
){
    constructor(billDTO: BillDTO): this(
        UUID.randomUUID(),
        billDTO.code,
        billDTO.value,
        billDTO.expireAt
    )
}