package br.com.sergioluigi.expenseregister.infra.database.repository

import br.com.sergioluigi.expenseregister.infra.database.entity.Bill
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BillRepository: JpaRepository<Bill,UUID> {

    fun findByCode(code: UUID): Bill
    fun existsByCode(code: UUID): Boolean
}