package br.com.sergioluigi.expenseregister.integration

import br.com.sergioluigi.expenseregister.ExpenseRegisterApplicationTests
import br.com.sergioluigi.expenseregister.infra.controller.ExpenseController
import br.com.sergioluigi.expenseregister.infra.database.repository.ExpenseRepository
import br.com.sergioluigi.expenseregister.infra.extension.findByIdOrNull
import br.com.sergioluigi.expenseregister.model.dto.ExpenseDTO
import br.com.sergioluigi.expenseregister.model.dto.ExpenseSaveUpdateDTO
import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterError.EXPENSE_NOT_FOUND
import br.com.sergioluigi.expenseregister.modules.expense.ExpenseServiceImpl
import br.com.sergioluigi.expenseregister.util.SQLExpenseTagIntegrationTest
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal
import java.util.*

@Testcontainers
@AutoConfigureWebTestClient
@ActiveProfiles("test-container")
@Import(ExpenseServiceImpl::class, ExpenseController::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = [ExpenseRegisterApplicationTests::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class ExpenseIntegrationTest{

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var expenseRepository: ExpenseRepository

    @Test
    fun `Given an expenseSaveUpdateDTO when call create then save expense and return a Mono of ExpenseDTO`(){

        val expenseSaveUpdateDTO = ExpenseSaveUpdateDTO(
            description = "Cats",
            responsibleName = "Me1",
            value = BigDecimal.valueOf(0.23),
            tagNames = mutableListOf("Tag4","Tag5","Tag6")
        )

        val result = webTestClient.post()
            .uri("/expenses")
            .contentType(APPLICATION_JSON)
            .body(BodyInserters.fromValue(expenseSaveUpdateDTO))
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody(ExpenseDTO::class.java)
            .returnResult()

        result.responseBody.shouldNotBeNull()

        expenseRepository.findByIdOrNull(result.responseBody!!.id!!).shouldNotBeNull()

    }

    @SQLExpenseTagIntegrationTest
    fun `Given an expenseSaveUpdateDTO and an existing expense id when call update then update the expense and return a Mono of ExpenseDTO`(){

        //this id is on expense_integration_test.sql
        val id = UUID.fromString("67ac4324-61e7-4e25-8206-7b91f032e597")

        val expense = expenseRepository.findByIdOrNull(id)!!

        val expenseSaveUpdateDTO = ExpenseSaveUpdateDTO(
            description = "Update",
            responsibleName = "responsibleName",
            value = BigDecimal.valueOf(0.23),
            tagNames = expense.tags.map { it.description }.toMutableList()
        )

        val result = webTestClient.put()
            .uri("/expenses/{id}", id)
            .contentType(APPLICATION_JSON)
            .body(BodyInserters.fromValue(expenseSaveUpdateDTO))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(ExpenseDTO::class.java)
            .returnResult()

        result.responseBody?.should {
           it.description.shouldBe(expenseSaveUpdateDTO.description)
           it.responsibleName.shouldBe(expenseSaveUpdateDTO.responsibleName)
           it.value.shouldBe(expenseSaveUpdateDTO.value)
           it.tags.map { tag -> tag.description }.shouldBe(expenseSaveUpdateDTO.tagNames)
        }

    }

    @Test
    fun `Given an expenseSaveUpdateDTO and a no existing expense id when call update then throw exception EXPENSE_NOT_FOUND`(){

        val id = UUID.randomUUID()

        val expenseSaveUpdateDTO = ExpenseSaveUpdateDTO(
            description = "Update",
            responsibleName = "responsibleName",
            value = BigDecimal.valueOf(0.23),
            tagNames = mutableListOf("1")
        )

        webTestClient.put()
            .uri("/expenses/{id}", id)
            .contentType(APPLICATION_JSON)
            .body(BodyInserters.fromValue(expenseSaveUpdateDTO))
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody()
            .jsonPath("$.status").isEqualTo(NOT_FOUND.value())
            .jsonPath("$.error").isEqualTo(NOT_FOUND.reasonPhrase)
            .jsonPath("$.message").isEqualTo(EXPENSE_NOT_FOUND.reason)
    }

    @SQLExpenseTagIntegrationTest
    fun `Given an id of an existing expense when call deleteById then the expense is deleted expense`(){

        //this id is on expense_integration_test.sql
        val id = UUID.fromString("d8632b04-0812-48df-9fef-8a784481779c")

        expenseRepository.existsById(id).shouldBeTrue()

        webTestClient.delete()
            .uri("/expenses/{id}", id)
            .exchange()
            .expectStatus()
            .isNoContent

        expenseRepository.existsById(id).shouldBeFalse()
    }

    @Test
    fun `Given an id of a no existing expense when call deleteById then throw exception EXPENSE_NOT_FOUND`(){

        webTestClient.delete()
            .uri("/expenses/{id}", UUID.randomUUID())
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody()
            .jsonPath("$.status").isEqualTo(NOT_FOUND.value())
            .jsonPath("$.error").isEqualTo(NOT_FOUND.reasonPhrase)
            .jsonPath("$.message").isEqualTo(EXPENSE_NOT_FOUND.reason)

    }

    @SQLExpenseTagIntegrationTest
    fun `Given a search == null and pageable when call findAll then return a Mono of paged ExpenseDTO`(){

        webTestClient.get()
            .uri("/expenses")
            .exchange()
            .expectStatus()
            .isOk

    }

    @SQLExpenseTagIntegrationTest
    fun `Given an id of an existing expense when call findById then return ExpenseDTO`(){

        webTestClient.get()
            .uri("/expenses/{id}", UUID.fromString("25286978-a847-4cd9-8771-e1538694e035"))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(ExpenseDTO::class.java)

    }

    @Test
    fun `Given an id of a no existing expense when call findById then throw exception EXPENSE_NOT_FOUND`(){

        webTestClient.get()
            .uri("/expenses/{id}", UUID.randomUUID())
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody()
            .jsonPath("$.status").isEqualTo(NOT_FOUND.value())
            .jsonPath("$.error").isEqualTo(NOT_FOUND.reasonPhrase)
            .jsonPath("$.message").isEqualTo(EXPENSE_NOT_FOUND.reason)

    }


}