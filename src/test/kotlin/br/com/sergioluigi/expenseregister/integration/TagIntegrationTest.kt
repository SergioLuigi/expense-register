package br.com.sergioluigi.expenseregister.integration

import br.com.sergioluigi.expenseregister.ExpenseRegisterApplicationTests
import br.com.sergioluigi.expenseregister.util.SQLExpenseTagIntegrationTest
import br.com.sergioluigi.expenseregister.infra.controller.TagController
import br.com.sergioluigi.expenseregister.model.dto.TagDTO
import br.com.sergioluigi.expenseregister.model.dto.TagSaveUpdateDTO
import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterError.*
import br.com.sergioluigi.expenseregister.modules.tag.TagServiceImpl
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus.*
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@AutoConfigureWebTestClient
@ActiveProfiles("test-container")
@Import(TagServiceImpl::class, TagController::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest(classes = [ExpenseRegisterApplicationTests::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class TagIntegrationTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun `Given a TagDTO with a new description when call create then save tag and return TagDTO`(){

        webTestClient.post()
            .uri("/tags")
            .contentType(APPLICATION_JSON)
            .body(BodyInserters.fromValue(TagSaveUpdateDTO("tag name")))
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody(TagDTO::class.java)

    }

    @SQLExpenseTagIntegrationTest
    fun `Given a TagDTO with a description that already exists when call create then throw exception TAG_ALREADY_EXISTS`(){

        webTestClient.post()
            .uri("/tags")
            .contentType(APPLICATION_JSON)
            .body(BodyInserters.fromValue(TagSaveUpdateDTO("Center")))
            .exchange()
            .expectStatus().isEqualTo(CONFLICT)
            .expectBody()
            .jsonPath("$.status").isEqualTo(CONFLICT.value())
            .jsonPath("$.error").isEqualTo(CONFLICT.reasonPhrase)
            .jsonPath("$.message").isEqualTo(TAG_ALREADY_EXISTS.reason)

    }

    @SQLExpenseTagIntegrationTest
    fun `Given the three first letters of a tag description when call findByFirstThreeLetters then return a Mono of Page of TagDTO`(){

        webTestClient.get()
            .uri{
                it.path("/tags")
                it.queryParam("letters","Lef")
                it.build()
            }
            .exchange()
            .expectStatus()
            .isOk

    }

    @Test
    fun `Given less then three letters of a tag description and a pageable object when call findByFirstThreeLetters then throw exception MINIMUM_THREE_LETTERS_NEEDED_FOR_SEARCH`(){

        webTestClient.get()
            .uri{
                it.path("/tags")
                it.queryParam("letters","")
                it.build()
            }
            .exchange()
            .expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
            .jsonPath("$.error").isEqualTo(BAD_REQUEST.reasonPhrase)
            .jsonPath("$.message").isEqualTo(MINIMUM_THREE_LETTERS_NEEDED_FOR_SEARCH.reason)

    }

    @SQLExpenseTagIntegrationTest
    fun `Given an existing tag description when call findByDescription then return a Mono of TagDTO`(){

        webTestClient.get()
            .uri("/tags/{description}","Left")
            .exchange()
            .expectStatus()
            .isOk
            .expectBody(TagDTO::class.java)

    }

    @Test
    fun `Given a no existing tag description when call findByDescription then return throw exception TAG_NOT_FOUND`(){

        webTestClient.get()
            .uri("/tags/{description}","this tag does not exists")
            .exchange()
            .expectStatus().isNotFound
            .expectBody()
            .jsonPath("$.status").isEqualTo(NOT_FOUND.value())
            .jsonPath("$.error").isEqualTo(NOT_FOUND.reasonPhrase)
            .jsonPath("$.message").isEqualTo(TAG_NOT_FOUND.reason)

    }


}