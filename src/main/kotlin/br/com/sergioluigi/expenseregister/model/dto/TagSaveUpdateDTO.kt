package br.com.sergioluigi.expenseregister.model.dto

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class TagSaveUpdateDTO(
    @field:NotBlank
    @field:Size(min = 3, max = 255)
    @JsonProperty("description")
    val description: String
)