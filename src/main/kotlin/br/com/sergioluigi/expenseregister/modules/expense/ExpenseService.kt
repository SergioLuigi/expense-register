package br.com.sergioluigi.expenseregister.modules.expense

import br.com.sergioluigi.expenseregister.infra.database.entity.Expense
import br.com.sergioluigi.expenseregister.infra.database.entity.Tag
import br.com.sergioluigi.expenseregister.model.dto.ExpenseDTO
import br.com.sergioluigi.expenseregister.model.dto.ExpenseSaveUpdateDTO
import br.com.sergioluigi.expenseregister.model.dto.SearchExpense
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Mono
import java.util.*

interface ExpenseService {

    /**
     * Create a new expense
     */
    fun create(expenseSaveUpdateDTO: ExpenseSaveUpdateDTO): Mono<ExpenseDTO>

    /**
     * Create new tags written during expense creation
     */
    fun saveTags(tagDescriptions: MutableList<String>): Mono<MutableList<Tag>>

    /**
     * Update an expense
     */
    fun update(id: UUID, expenseSaveUpdateDTO: ExpenseSaveUpdateDTO): Mono<ExpenseDTO>

    /**
     * Update expense tag list
     */
    fun updateTags(expense: Expense, tagNames: MutableList<String>): Expense

    /**
     * Delete expense by id
     */
    fun deleteById(id: UUID): Mono<Void>

    /**
     * Search a page of expenses through search parameter (not required)
     */
    fun findAll(search: SearchExpense?, pageable: Pageable): Mono<Page<ExpenseDTO>>

    /**
     * Search an expense by id
     */
    fun findById(id: UUID): Mono<ExpenseDTO>

}