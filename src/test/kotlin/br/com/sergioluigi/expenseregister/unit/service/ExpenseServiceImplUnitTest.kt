package br.com.sergioluigi.expenseregister.unit.service

import br.com.sergioluigi.expenseregister.extension.isExceptionErrorEqual
import br.com.sergioluigi.expenseregister.infra.database.entity.Expense
import br.com.sergioluigi.expenseregister.infra.database.entity.Tag
import br.com.sergioluigi.expenseregister.infra.database.repository.ExpenseRepository
import br.com.sergioluigi.expenseregister.infra.database.repository.TagRepository
import br.com.sergioluigi.expenseregister.infra.extension.findByIdOrNull
import br.com.sergioluigi.expenseregister.model.dto.ExpenseDTO
import br.com.sergioluigi.expenseregister.model.dto.ExpenseSaveUpdateDTO
import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterError.EXPENSE_NOT_FOUND
import br.com.sergioluigi.expenseregister.modules.expense.ExpenseServiceImpl
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import reactor.kotlin.core.publisher.toMono
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockKExtension::class)
internal class ExpenseServiceImplUnitTest {

    @MockK
    private lateinit var expenseRepository: ExpenseRepository

    @MockK
    private lateinit var tagRepository: TagRepository

    @InjectMockKs
    private lateinit var expenseService: ExpenseServiceImpl

    private lateinit var newTagNames: MutableList<String>

    private lateinit var alreadyCreatedTagNames: MutableList<String>

    private lateinit var alreadyCreatedTags: MutableList<Tag>

    private lateinit var newTags: MutableList<Tag>

    private lateinit var expenseSaveUpdateDTO: ExpenseSaveUpdateDTO

    private lateinit var basicExpense: Expense

    @BeforeEach
    fun beforeEach(){

        newTagNames = mutableListOf("Car","Lunch","House")

        alreadyCreatedTagNames = mutableListOf("Computer","Kindle","Mug")

        newTags = newTagNames.map { Tag(it) }.toMutableList()

        alreadyCreatedTags = alreadyCreatedTagNames.map { Tag(it) }.toMutableList()

        expenseSaveUpdateDTO = ExpenseSaveUpdateDTO(
            description = "Gatos",
            responsibleName = "Eu",
            value = BigDecimal.valueOf(0.23)
        )

        expenseSaveUpdateDTO.tagNames.addAll(alreadyCreatedTagNames)

        basicExpense = Expense(expenseSaveUpdateDTO, alreadyCreatedTags)

        basicExpense.createdDate = LocalDateTime.now()


    }

    @Test
    fun `Given an expenseSaveUpdateDTO when call create then save expense and return a Mono of ExpenseDTO`(){

        val alreadyCreatedTagsMono = alreadyCreatedTags.toMono()

        every { expenseService.saveTags(expenseSaveUpdateDTO.tagNames) } returns alreadyCreatedTagsMono

        every { tagRepository.findAllByDescriptionIn(expenseSaveUpdateDTO.tagNames) } returns mutableListOf()

        every { tagRepository.saveAll(alreadyCreatedTags) } returns alreadyCreatedTags

        val expense = Expense(expenseSaveUpdateDTO, alreadyCreatedTagsMono.block()!!)

        every { expenseRepository.save(expense) } returns expense

        expense.createdDate = LocalDateTime.now()

        val expenseDTO = ExpenseDTO(expense)

        StepVerifier.create(expenseService.create(expenseSaveUpdateDTO))
            .expectSubscription()
            .expectNext(expenseDTO)
            .verifyComplete()

    }

    @Test
    fun `Given an array of new tag names when call saveTags then return list of new tags`(){

            every { tagRepository.findAllByDescriptionIn(any()) } returns mutableListOf()

            every { tagRepository.saveAll(newTags) } returns newTags

        StepVerifier.create(expenseService.saveTags(newTagNames))
            .expectSubscription()
            .expectNext(newTags)
            .verifyComplete()
    }

    @Test
    fun `Given an array of new tag names and already registered tag names when call saveTags then return list of all tags`(){

        val tagDescriptions = newTagNames.plus(alreadyCreatedTagNames).toMutableList()

        every { tagRepository.findAllByDescriptionIn(tagDescriptions) } returns alreadyCreatedTags

        every { tagRepository.saveAll(newTags) } returns newTags

        val mixedTags = alreadyCreatedTags.plus(newTags).toMutableList()

        StepVerifier.create(expenseService.saveTags(tagDescriptions))
            .expectSubscription()
            .expectNext(mixedTags)
            .verifyComplete()
    }

    @Test
    fun `Given an expense and the list of its tag names when call updateTags then expense tag list must stay like it is`(){

        val result = expenseService.updateTags(basicExpense, alreadyCreatedTagNames)

        result.tags.shouldBe(alreadyCreatedTags)

    }

    @Test
    fun `Given an expense and a list of new tag names, which none belongs to any tag and are not related to the expense when call updateTags then expense tag list must to have only tags with those new names`(){

        every { tagRepository.findAllByDescriptionIn(any()) } returns mutableListOf()

        every { tagRepository.saveAll(newTags) } returns newTags

        every { expenseRepository.save(basicExpense) } returns basicExpense

        val result = expenseService.updateTags(basicExpense, newTagNames)

        result.tags.shouldBe(newTags)

    }

    @Test
    fun `Given an expense and a list of tag names, which some belongs to already created tags and some are related to the expense when call updateTags then expense tag list must to have only tags with those names`(){

        every { tagRepository.findAllByDescriptionIn(any()) } returns alreadyCreatedTags

        every { tagRepository.saveAll(newTags) } returns newTags

        val mixedTags = alreadyCreatedTags.plus(newTags).toMutableList()

        every { expenseRepository.save(basicExpense) } returns basicExpense

        val result = expenseService.updateTags(basicExpense, alreadyCreatedTagNames.plus(newTagNames).toMutableList())

        result.tags.shouldBe(mixedTags)

    }

    @Test
    fun `Given an expenseSaveUpdateDTO and an existing expense id when call update then update the expense and return a Mono of ExpenseDTO`(){

        every { expenseRepository.findByIdOrNull(any()) } returns basicExpense

        every { tagRepository.findAllByDescriptionIn(any()) } returns mutableListOf()

        val id = UUID.randomUUID()

        basicExpense.id = id

        every { expenseRepository.save(basicExpense) } returns basicExpense

        val expenseDTO = ExpenseDTO(basicExpense)

        StepVerifier.create(expenseService.update(id, expenseSaveUpdateDTO))
            .expectSubscription()
            .expectNext(expenseDTO)
            .verifyComplete()

    }

    @Test
    fun `Given an expenseSaveUpdateDTO and a no existing expense id when call update then throw exception EXPENSE_NOT_FOUND`(){

        every { expenseRepository.findByIdOrNull(any()) } returns null

        StepVerifier.create(expenseService.update(UUID.randomUUID(), expenseSaveUpdateDTO))
            .expectSubscription()
            .expectErrorMatches {
                it.isExceptionErrorEqual(EXPENSE_NOT_FOUND)
            }
            .verify()
    }

    @Test
    fun `Given an id of an existing expense when call deleteById then delete expense`(){

        every { expenseRepository.findByIdOrNull(any()) } returns basicExpense

        justRun { expenseRepository.deleteById(any()) }

        StepVerifier.create(expenseService.deleteById(UUID.randomUUID()))
            .expectSubscription()
            .verifyComplete()
    }

    @Test
    fun `Given an id of a no existing expense when call deleteById then throw exception EXPENSE_NOT_FOUND`(){

        every { expenseRepository.findByIdOrNull(any()) } returns null

        StepVerifier.create(expenseService.deleteById(UUID.randomUUID()))
            .expectSubscription()
            .expectErrorMatches {
                it.isExceptionErrorEqual(EXPENSE_NOT_FOUND)
            }
            .verify()
    }

    @Test
    fun `Given a search == null and pageable when call findAll then return a Mono of paged ExpenseDTO`(){

        val pageable = PageRequest.of(0,10)

        val page: PageImpl<Expense> = PageImpl(listOf(basicExpense),pageable,1)

        every { expenseRepository.findAll(any(), pageable) } returns page

        val pageDTO = page.map { expense -> ExpenseDTO(expense) }

        StepVerifier.create(expenseService.findAll(null, pageable))
            .expectSubscription()
            .expectNext(pageDTO)
            .verifyComplete()
    }

    @Test
    fun `Given an id of an existing expense when call findById then return ExpenseDTO`(){

        val id = UUID.randomUUID()

        every { expenseRepository.findByIdOrNull(id) } returns basicExpense

        val expenseDTO = ExpenseDTO(basicExpense)

        StepVerifier.create(expenseService.findById(id))
            .expectSubscription()
            .expectNext(expenseDTO)
            .verifyComplete()

    }

    @Test
    fun `Given an id of a no existing expense when call findById then throw exception EXPENSE_NOT_FOUND`(){

        every { expenseRepository.findByIdOrNull(any()) } returns null

        StepVerifier.create(expenseService.findById(UUID.randomUUID()))
            .expectSubscription()
            .expectErrorMatches {
                it.isExceptionErrorEqual(EXPENSE_NOT_FOUND)
            }
            .verify()
    }


}