package br.com.sergioluigi.expenseregister.modules.expense

import br.com.sergioluigi.expenseregister.infra.database.entity.Expense
import br.com.sergioluigi.expenseregister.infra.database.entity.Tag
import br.com.sergioluigi.expenseregister.infra.database.repository.ExpenseRepository
import br.com.sergioluigi.expenseregister.infra.database.repository.TagRepository
import br.com.sergioluigi.expenseregister.infra.database.specification.ExpenseSpecification
import br.com.sergioluigi.expenseregister.model.dto.ExpenseDTO
import br.com.sergioluigi.expenseregister.model.dto.ExpenseSaveUpdateDTO
import br.com.sergioluigi.expenseregister.model.dto.SearchExpense
import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterError.EXPENSE_NOT_FOUND
import br.com.sergioluigi.expenseregister.infra.extension.*
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.util.*

@Service
class ExpenseServiceImpl(
    private val expenseRepository: ExpenseRepository,
    private val tagRepository: TagRepository,
): ExpenseService {

    private val logger = KotlinLogging.logger {  }

    @Transactional
    override fun create(expenseSaveUpdateDTO: ExpenseSaveUpdateDTO): Mono<ExpenseDTO> =
        Mono.just(expenseSaveUpdateDTO)
            .zipWhen { saveTags(expenseSaveUpdateDTO.tagNames) }
            .map { Expense(it.t1, it.t2) }
            .map { expenseRepository.save(it) }
            .map { ExpenseDTO(it) }
            .logCreation(logger)

    override fun saveTags(tagDescriptions: MutableList<String>): Mono<MutableList<Tag>> =
        Flux.just( tagRepository.findAllByDescriptionIn(tagDescriptions) )
            .doOnNext {
                tagDescriptions.removeAll(it.map { tag -> tag.description })
            }
            .map {

                val newTagsToSave = tagDescriptions.distinct()
                    .map { tagDescription -> Tag(tagDescription) }

                val newTags = tagRepository.saveAll(newTagsToSave)

                it.plus(newTags).toMutableList()

            }
            .toMono()

    @Transactional
    override fun update(id: UUID, expenseSaveUpdateDTO: ExpenseSaveUpdateDTO): Mono<ExpenseDTO> =
        throwIfNull(EXPENSE_NOT_FOUND){ expenseRepository.findByIdOrNull(id) }
            .zipWhenToMono{ updateTags(it , expenseSaveUpdateDTO.tagNames) }
            .map { it.t1.update(expenseSaveUpdateDTO) }
            .map { expenseRepository.save(it) }
            .map { ExpenseDTO(it) }
            .logUpdate(logger)

    override fun updateTags(expense: Expense, tagNames: MutableList<String>): Expense {

        val hasTagListChanged = expense.tags.removeIf { !tagNames.contains(it.description) }.or(tagNames.size != expense.tags.size)

        if(hasTagListChanged){

            val addsTagsDescription = if(expense.tags.isNotEmpty()){
                tagNames.filter { !expense.tags.map { tag -> tag.description }.contains(it) }.toMutableList()
            }else{
                tagNames
            }

            val alreadyCreatedTags = tagRepository.findAllByDescriptionIn(addsTagsDescription)

            addsTagsDescription.removeAll(alreadyCreatedTags.map { tag -> tag.description })

            if (addsTagsDescription.isNotEmpty()) {

                var newTags = mutableListOf<Tag>()

                addsTagsDescription.forEach { newTags.add(Tag(it)) }

                newTags = tagRepository.saveAll(newTags)

                expense.tags.addAll(newTags)
            }

            expenseRepository.save(expense)
        }

        return expense
    }

    @Transactional
    override fun deleteById(id: UUID): Mono<Void> =
        throwIfNull(EXPENSE_NOT_FOUND){ expenseRepository.findByIdOrNull(id) }
            .doOnNext { expenseRepository.deleteById(id) }
            .logDelete(logger, id, Expense::class)
            .then()

    override fun findAll(search: SearchExpense?, pageable: Pageable): Mono<Page<ExpenseDTO>> =
        Mono.just(expenseRepository.findAll(ExpenseSpecification().search(search), pageable))
            .map { it.map { page -> ExpenseDTO(page) } }

    override fun findById(id: UUID): Mono<ExpenseDTO> =
        throwIfNull(EXPENSE_NOT_FOUND){ expenseRepository.findByIdOrNull(id) }
            .map { ExpenseDTO(it) }

}