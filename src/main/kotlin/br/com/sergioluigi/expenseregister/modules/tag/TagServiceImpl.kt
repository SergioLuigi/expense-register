package br.com.sergioluigi.expenseregister.modules.tag

import br.com.sergioluigi.expenseregister.infra.database.entity.Tag
import br.com.sergioluigi.expenseregister.infra.database.repository.TagRepository
import br.com.sergioluigi.expenseregister.infra.database.specification.TagSpecification
import br.com.sergioluigi.expenseregister.infra.extension.logCreation
import br.com.sergioluigi.expenseregister.infra.extension.takeIfOrThrow
import br.com.sergioluigi.expenseregister.infra.extension.throwIfNull
import br.com.sergioluigi.expenseregister.infra.extension.validate
import br.com.sergioluigi.expenseregister.model.dto.TagDTO
import br.com.sergioluigi.expenseregister.model.dto.TagSaveUpdateDTO
import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterError.*
import mu.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class TagServiceImpl(
    private val tagRepository: TagRepository
): TagService {

    private val logger = KotlinLogging.logger {  }

    override fun create(tagSaveUpdateDTO: TagSaveUpdateDTO): Mono<TagDTO> =
        tagSaveUpdateDTO
            .toMono()
            .validate(TAG_ALREADY_EXISTS){ !tagRepository.existsByDescription(tagSaveUpdateDTO.description) }
            .map { Tag(tagSaveUpdateDTO.description) }
            .map { tagRepository.save(it) }
            .map { TagDTO(it) }
            .logCreation(logger)

    override fun findByDescription(description: String): Mono<TagDTO> =
        throwIfNull(TAG_NOT_FOUND) { tagRepository.findByDescription(description) }
            .map { TagDTO(it) }

    override fun findByFirstThreeLetters(letters: String, pageable: Pageable): Mono<Page<TagDTO>> =
        letters.takeIfOrThrow(MINIMUM_THREE_LETTERS_NEEDED_FOR_SEARCH) { it.length >= 3 }
            .toMono()
            .map { tagRepository.findAll(TagSpecification().searchByFirstThreeLetters(it), pageable) }
            .map { it.map { tag -> TagDTO(tag) } }




}