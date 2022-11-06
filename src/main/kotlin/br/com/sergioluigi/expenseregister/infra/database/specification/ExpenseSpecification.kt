package br.com.sergioluigi.expenseregister.infra.database.specification

import br.com.sergioluigi.expenseregister.infra.database.entity.Expense
import br.com.sergioluigi.expenseregister.model.dto.SearchExpense
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class ExpenseSpecification {

    fun search(searchExpense: SearchExpense?): Specification<Expense>? =
        searchExpense?.let {
            Specification.where(findByTerm(searchExpense.term))
                .or(findByPeriod(searchExpense.startDate, searchExpense.finalDate))
        }

    private fun findByTerm(term: String?): Specification<Expense>? = term?.let{
        Specification{ root, _, cb ->
            val value = it.toBigDecimalOrNull()
            cb.or(
                cb.like(cb.upper(root.get(Expense::description.name)),"%${term.uppercase()}%"),
                cb.like(cb.upper(root.get(Expense::responsibleName.name)),"%${term.uppercase()}%"),
                if(value != null) {
                    cb.equal(root.get<BigDecimal>(Expense::value.name), value)
                }else{
                    cb.conjunction()
                }
            )
        }
    }

    private fun findByPeriod(initialDate: LocalDate?,
                             finalDate: LocalDate?): Specification<Expense> =
        Specification{ root, _, cb ->
            val localDateCreatedDate = root.get<LocalDateTime>(Expense::createdDate.name).`as`(LocalDate::class.java)
            when{
                initialDate == null && finalDate == null -> cb.conjunction()
                initialDate == null -> cb.between(localDateCreatedDate,LocalDate.EPOCH, finalDate)
                else -> cb.between(localDateCreatedDate,initialDate, LocalDate.now())
            }

        }

}