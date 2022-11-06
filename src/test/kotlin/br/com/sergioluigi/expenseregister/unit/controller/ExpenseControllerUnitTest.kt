package br.com.sergioluigi.expenseregister.unit.controller

import br.com.sergioluigi.expenseregister.config.PageableWebFluxConfig
import br.com.sergioluigi.expenseregister.infra.controller.ExpenseController
import br.com.sergioluigi.expenseregister.infra.database.entity.Expense
import br.com.sergioluigi.expenseregister.infra.database.entity.Tag
import br.com.sergioluigi.expenseregister.infra.database.repository.ExpenseRepository
import br.com.sergioluigi.expenseregister.infra.database.repository.TagRepository
import br.com.sergioluigi.expenseregister.infra.extension.findByIdOrNull
import br.com.sergioluigi.expenseregister.model.dto.ExpenseDTO
import br.com.sergioluigi.expenseregister.model.dto.ExpenseSaveUpdateDTO
import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterError.EXPENSE_NOT_FOUND
import br.com.sergioluigi.expenseregister.modules.expense.ExpenseServiceImpl
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.justRun
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.kotlin.core.publisher.toMono
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@ExtendWith(SpringExtension::class)
@WebFluxTest(ExpenseController::class)
@Import(ExpenseServiceImpl::class, PageableWebFluxConfig::class)
class ExpenseControllerUnitTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockK
    private lateinit var expenseService: ExpenseServiceImpl

    @MockkBean
    private lateinit var expenseRepository: ExpenseRepository

    @MockkBean
    private lateinit var tagRepository: TagRepository

    private lateinit var expenseSaveUpdateDTO: ExpenseSaveUpdateDTO

    private lateinit var alreadyCreatedTagNames: MutableList<String>

    private lateinit var alreadyCreatedTags: MutableList<Tag>

    private lateinit var basicExpense: Expense

    @BeforeEach
    fun beforeEach(){

        alreadyCreatedTagNames = mutableListOf("Computer","Kindle","Mug")

        alreadyCreatedTags = alreadyCreatedTagNames.map { Tag(it) }.toMutableList()

        expenseSaveUpdateDTO = ExpenseSaveUpdateDTO(
            description = "Gatos",
            responsibleName = "Eu1",
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

        webTestClient.post()
            .uri("/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(expenseSaveUpdateDTO))
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody(ExpenseDTO::class.java)

    }

    @Test
    fun `Given an expenseSaveUpdateDTO and an existing expense id when call update then update the expense and return a Mono of ExpenseDTO`(){

        every { expenseRepository.findByIdOrNull(any()) } returns basicExpense

        every { tagRepository.findAllByDescriptionIn(any()) } returns mutableListOf()

        val id = UUID.randomUUID()

        basicExpense.id = id

        every { expenseRepository.save(basicExpense) } returns basicExpense

        webTestClient.put()
            .uri("/expenses/{id}",id)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(expenseSaveUpdateDTO))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(ExpenseDTO::class.java)

    }

    @Test
    fun `Given an expenseSaveUpdateDTO and a no existing expense id when call update then throw exception EXPENSE_NOT_FOUND`(){

        every { expenseRepository.findByIdOrNull(any()) } returns null

        webTestClient.put()
            .uri("/expenses/{id}",UUID.randomUUID())
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(expenseSaveUpdateDTO))
            .exchange()
            .expectStatus().isNotFound
            .expectBody()
            .jsonPath("$.status").isEqualTo(NOT_FOUND.value())
            .jsonPath("$.error").isEqualTo(NOT_FOUND.reasonPhrase)
            .jsonPath("$.message").isEqualTo(EXPENSE_NOT_FOUND.reason)
    }

    @Test
    fun `Given an id of an existing expense when call deleteById then delete expense`(){

        every { expenseRepository.findByIdOrNull(any()) } returns basicExpense

        justRun { expenseRepository.deleteById(any()) }

        webTestClient.delete()
            .uri("/expenses/{id}", UUID.randomUUID())
            .exchange()
            .expectStatus()
            .isNoContent
    }

    @Test
    fun `Given an id of a no existing expense when call deleteById then throw exception EXPENSE_NOT_FOUND`(){

        every { expenseRepository.findByIdOrNull(any()) } returns null

        webTestClient.delete()
            .uri("/expenses/{id}", UUID.randomUUID())
            .exchange()
            .expectStatus().isNotFound
            .expectBody()
            .jsonPath("$.status").isEqualTo(NOT_FOUND.value())
            .jsonPath("$.error").isEqualTo(NOT_FOUND.reasonPhrase)
            .jsonPath("$.message").isEqualTo(EXPENSE_NOT_FOUND.reason)
    }

    @Test
    fun `Given a search == null and pageable when call findAll then return a Mono of paged ExpenseDTO`(){

        val pageable = PageRequest.of(0,10, Sort.by(Sort.Direction.ASC,"createdDate"))

        val page: Page<Expense> = PageImpl(listOf(basicExpense),pageable,1)

        every { expenseRepository.findAll(any(), pageable) } returns page

        webTestClient.get()
            .uri{
                it.path("/expenses")
                it.build()
            }
            .exchange()
            .expectStatus()
            .isOk
    }

    @Test
    fun `Given an id of an existing expense when call findById then return ExpenseDTO`(){

        val id = UUID.randomUUID()

        every { expenseRepository.findByIdOrNull(id) } returns basicExpense

        webTestClient.get()
            .uri("/expenses/{id}", id)
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(ExpenseDTO::class.java)

    }

    @Test
    fun `Given an id of a no existing expense when call findById then throw exception EXPENSE_NOT_FOUND`(){

        every { expenseRepository.findByIdOrNull(any()) } returns null

        webTestClient.get()
            .uri("/expenses/{id}", UUID.randomUUID())
            .exchange()
            .expectStatus().isNotFound
            .expectBody()
            .jsonPath("$.status").isEqualTo(NOT_FOUND.value())
            .jsonPath("$.error").isEqualTo(NOT_FOUND.reasonPhrase)
            .jsonPath("$.message").isEqualTo(EXPENSE_NOT_FOUND.reason)
    }
}