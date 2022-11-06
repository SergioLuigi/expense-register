package br.com.sergioluigi.expenseregister.modules.tag

import br.com.sergioluigi.expenseregister.model.dto.TagDTO
import br.com.sergioluigi.expenseregister.model.dto.TagSaveUpdateDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Mono

interface TagService {

    fun create(tagSaveUpdateDTO: TagSaveUpdateDTO): Mono<TagDTO>
    fun findByDescription(description: String): Mono<TagDTO>
    fun findByFirstThreeLetters(letters: String, pageable: Pageable): Mono<Page<TagDTO>>
}