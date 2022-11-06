package br.com.sergioluigi.expenseregister.infra.database.repository

import br.com.sergioluigi.expenseregister.infra.database.entity.Expense
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface ExpenseRepository: JpaRepository<Expense, UUID>, JpaSpecificationExecutor<Expense> {



}