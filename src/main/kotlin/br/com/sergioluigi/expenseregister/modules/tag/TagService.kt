package br.com.sergioluigi.expenseregister.modules.tag

import br.com.sergioluigi.expenseregister.model.dto.TagDTO
import br.com.sergioluigi.expenseregister.model.dto.TagSaveUpdateDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Mono

interface TagService {

    /**
     * Create a new tag
     */
    fun create(tagSaveUpdateDTO: TagSaveUpdateDTO): Mono<TagDTO>

    /**
     * Find tag by description
     */
    fun findByDescription(description: String): Mono<TagDTO>

    /**
     * Find tag by the first three letters of description
     */
    fun findByFirstThreeLetters(letters: String, pageable: Pageable): Mono<Page<TagDTO>>
}