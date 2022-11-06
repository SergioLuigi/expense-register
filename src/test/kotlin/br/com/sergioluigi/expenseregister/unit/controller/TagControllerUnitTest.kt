package br.com.sergioluigi.expenseregister.unit.controller

import br.com.sergioluigi.expenseregister.config.PageableWebFluxConfig
import br.com.sergioluigi.expenseregister.infra.controller.TagController
import br.com.sergioluigi.expenseregister.infra.database.entity.Tag
import br.com.sergioluigi.expenseregister.infra.database.repository.TagRepository
import br.com.sergioluigi.expenseregister.model.dto.TagDTO
import br.com.sergioluigi.expenseregister.model.dto.TagSaveUpdateDTO
import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterError.*
import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterException
import br.com.sergioluigi.expenseregister.modules.tag.TagServiceImpl
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.impl.annotations.MockK
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
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import reactor.kotlin.core.publisher.toMono
import java.util.*

@WebFluxTest(TagController::class)
@ExtendWith(SpringExtension::class)
@Import(TagServiceImpl::class, PageableWebFluxConfig::class)
class TagControllerUnitTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockK
    private lateinit var tagService: TagServiceImpl

    @MockkBean
    private lateinit var tagRepository: TagRepository

    private lateinit var tagSaveUpdateDTO: TagSaveUpdateDTO

    private lateinit var tagDTO: TagDTO

    @BeforeEach
    fun beforeEach(){
        tagSaveUpdateDTO = TagSaveUpdateDTO("tag save update DTO")
        tagDTO = TagDTO(UUID.randomUUID(), tagSaveUpdateDTO.description)
    }

    @Test
    fun `Given a TagDTO with a new description when call create then save tag and return TagDTO`(){

        every { tagRepository.existsByDescription(tagSaveUpdateDTO.description) } returns false

        val tag = Tag(tagSaveUpdateDTO.description)

        every { tagRepository.save(tag) } returns tag

        every { tagService.create(tagSaveUpdateDTO) } returns tagDTO.toMono()

        webTestClient.post()
            .uri("/tags")
            .contentType(APPLICATION_JSON)
            .body(BodyInserters.fromValue(tagSaveUpdateDTO))
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody(TagDTO::class.java)

    }

    @Test
    fun `Given a TagDTO with a description that already exists when call create then throw exception TAG_ALREADY_EXISTS`(){

        every { tagRepository.existsByDescription(tagSaveUpdateDTO.description) } returns true

        webTestClient.post()
            .uri("/tags")
            .contentType(APPLICATION_JSON)
            .body(BodyInserters.fromValue(tagSaveUpdateDTO))
            .exchange()
            .expectStatus().isEqualTo(CONFLICT)
            .expectBody()
            .jsonPath("$.status").isEqualTo(CONFLICT.value())
            .jsonPath("$.error").isEqualTo(CONFLICT.reasonPhrase)
            .jsonPath("$.message").isEqualTo(TAG_ALREADY_EXISTS.reason)

    }

    @Test
    fun `Given an existing tag description when call findByDescription then return a Mono of TagDTO`(){

        val tag = Tag(tagSaveUpdateDTO.description)

        every { tagRepository.findByDescription(tagSaveUpdateDTO.description) } returns tag

        val tagDTO = TagDTO(tag)

        every { tagService.findByDescription(tagSaveUpdateDTO.description) } returns tagDTO.toMono()

        webTestClient.get()
            .uri("/tags/{description}",tagSaveUpdateDTO.description)
            .exchange()
            .expectStatus()
            .isOk

    }

    @Test
    fun `Given a no existing tag description when call findByDescription then return throw exception TAG_NOT_FOUND`(){

        every { tagRepository.findByDescription(any()) } returns null

        webTestClient.get()
            .uri("/tags/{description}",tagSaveUpdateDTO.description)
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody()
            .jsonPath("$.status").isEqualTo(NOT_FOUND.value())
            .jsonPath("$.error").isEqualTo(NOT_FOUND.reasonPhrase)
            .jsonPath("$.message").isEqualTo(TAG_NOT_FOUND.reason)
    }

    @Test
    fun `Given the three first letters of a tag description when call findByFirstThreeLetters then return a Mono of Page of TagDTO`(){

        val pageable = PageRequest.of(0,10, Sort.by(Sort.Direction.ASC,"createdDate"))

        val tag = Tag(tagSaveUpdateDTO.description)

        val page: Page<Tag> = PageImpl(listOf(tag),pageable,1)

        val pageDTO = page.map { tag1 -> TagDTO(tag1) }.toMono()

        every { tagRepository.findAll(any(), pageable) } returns page

        every { tagService.findByFirstThreeLetters(tagSaveUpdateDTO.description, pageable) } returns pageDTO

        webTestClient.get()
            .uri{
                it.path("/tags")
                it.queryParam("letters","tag save update DTO")
                it.build()
            }
            .exchange()
            .expectStatus()
            .isOk

    }

    @Test
    fun `Given less then three letters of a tag description and a pageable object when call findByFirstThreeLetters then throw exception MINIMUM_THREE_LETTERS_NEEDED_FOR_SEARCH`(){

        val pageable = PageRequest.of(0,10)

        every { tagService.findByFirstThreeLetters(tagSaveUpdateDTO.description, pageable) } throws ExpenseRegisterException(MINIMUM_THREE_LETTERS_NEEDED_FOR_SEARCH)

        webTestClient.get()
            .uri{
                it.path("/tags")
                it.queryParam("letters",tagSaveUpdateDTO.description.substring(0,1))
                it.build()
            }
            .exchange()
            .expectStatus().isBadRequest
            .expectBody()
            .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.value())
            .jsonPath("$.error").isEqualTo(HttpStatus.BAD_REQUEST.reasonPhrase)
            .jsonPath("$.message").isEqualTo(MINIMUM_THREE_LETTERS_NEEDED_FOR_SEARCH.reason)

    }
}