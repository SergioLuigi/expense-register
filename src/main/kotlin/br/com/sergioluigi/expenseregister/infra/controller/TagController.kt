package br.com.sergioluigi.expenseregister.infra.controller

import br.com.sergioluigi.expenseregister.model.dto.TagDTO
import br.com.sergioluigi.expenseregister.model.dto.TagSaveUpdateDTO
import br.com.sergioluigi.expenseregister.modules.tag.TagService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import javax.validation.Valid

@RestController
@RequestMapping("/tags")
class TagController(
    private val tagService: TagService
) {

    @PostMapping
    @ResponseStatus(CREATED)
    @Operation(summary = "Create a tag.")
    fun create(@RequestBody @Valid tagSaveUpdateDTO: TagSaveUpdateDTO): Mono<TagDTO> =
        tagService.create(tagSaveUpdateDTO)

    @GetMapping
    @ResponseStatus(OK)
    @Operation(summary = "Return a page of tags which starts by the first three letters.")
    fun findByFirstThreeLetters(@RequestParam letters: String,
                                @PageableDefault(sort = ["createdDate"]) pageable: Pageable): Mono<Page<TagDTO>> =
        tagService.findByFirstThreeLetters(letters, pageable)


    @ResponseStatus(OK)
    @GetMapping("/{description}")
    @Operation(summary = "Return a tag by name.")
    fun findByDescription(@PathVariable description: String): Mono<TagDTO> =
        tagService.findByDescription(description)

}