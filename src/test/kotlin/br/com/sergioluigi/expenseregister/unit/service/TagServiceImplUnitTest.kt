package br.com.sergioluigi.expenseregister.unit.service

import br.com.sergioluigi.expenseregister.extension.isExceptionErrorEqual
import br.com.sergioluigi.expenseregister.infra.database.entity.Tag
import br.com.sergioluigi.expenseregister.infra.database.repository.TagRepository
import br.com.sergioluigi.expenseregister.model.dto.TagDTO
import br.com.sergioluigi.expenseregister.model.dto.TagSaveUpdateDTO
import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterError.*
import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterException
import br.com.sergioluigi.expenseregister.modules.tag.TagServiceImpl
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import reactor.test.StepVerifier
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockKExtension::class)
class TagServiceImplUnitTest {

    @MockK
    private lateinit var tagRepository: TagRepository

    @InjectMockKs
    private lateinit var tagServiceImpl: TagServiceImpl

    private lateinit var tagSaveUpdateDTO: TagSaveUpdateDTO

    private lateinit var tag: Tag

    @BeforeEach
    fun beforeEach(){
        tagSaveUpdateDTO = TagSaveUpdateDTO("tag name")
        tag = Tag(tagSaveUpdateDTO.description)
        tag.createdDate = LocalDateTime.now()
    }

    @Test
    fun `Given a TagDTO with a new description when call create then save tag and return TagDTO`(){

        every { tagRepository.existsByDescription(tagSaveUpdateDTO.description) } returns false

        val tag = Tag(tagSaveUpdateDTO.description)

        every { tagRepository.save(tag) } returns tag

        val tagDTO = TagDTO(tag)

        StepVerifier.create(tagServiceImpl.create(tagSaveUpdateDTO))
            .expectSubscription()
            .expectNext(tagDTO)
            .verifyComplete()

    }

    @Test
    fun `Given a TagDTO with a description that already exists when call create then throw exception TAG_ALREADY_EXISTS`(){

        every { tagRepository.existsByDescription(tagSaveUpdateDTO.description) } returns true

        StepVerifier.create(tagServiceImpl.create(tagSaveUpdateDTO))
            .expectSubscription()
            .expectErrorMatches {
                it.isExceptionErrorEqual(TAG_ALREADY_EXISTS)
            }.verify()

    }

    @Test
    fun `Given an existing tag description when call findByDescription then return a Mono of TagDTO`(){

        every { tagRepository.findByDescription(tag.description) } returns tag

        val tagDTO = TagDTO(tag)

        StepVerifier.create(tagServiceImpl.findByDescription(tag.description))
            .expectSubscription()
            .expectNext(tagDTO)
            .verifyComplete()

    }

    @Test
    fun `Given a no existing tag description when call findByDescription then return throw exception TAG_NOT_FOUND`(){

        every { tagRepository.findByDescription(any()) } returns null

        StepVerifier.create(tagServiceImpl.findByDescription(tag.description))
            .expectSubscription()
            .expectErrorMatches {
                it.isExceptionErrorEqual(TAG_NOT_FOUND)
            }.verify()

    }

    @Test
    fun `Given the three first letters of a tag description and a pageable object when call findByFirstThreeLetters then return a Mono of Page of TagDTO`(){

        val pageable = PageRequest.of(0,10)

        val page: Page<Tag> = PageImpl(listOf(tag),pageable,1)

        val pageDTO = page.map { tag -> TagDTO(tag) }

        every { tagRepository.findAll(any(), pageable) } returns page

        StepVerifier.create(tagServiceImpl.findByFirstThreeLetters(tag.description, pageable))
            .expectSubscription()
            .expectNext(pageDTO)
            .verifyComplete()

    }

    @Test
    fun `Given less then three letters of a tag description and a pageable object when call findByFirstThreeLetters then throw exception MINIMUM_THREE_LETTERS_NEEDED_FOR_SEARCH`(){

        val pageable = PageRequest.of(0,10)

        val page: Page<Tag> = PageImpl(listOf(tag),pageable,1)

        val exception = shouldThrow<ExpenseRegisterException> {
            tagServiceImpl.findByFirstThreeLetters(tag.description.substring(0,1), pageable)
        }

        exception.error.shouldBe(MINIMUM_THREE_LETTERS_NEEDED_FOR_SEARCH)

    }

}