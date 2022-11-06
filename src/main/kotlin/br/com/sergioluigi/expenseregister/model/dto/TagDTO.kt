package br.com.sergioluigi.expenseregister.model.dto

import br.com.sergioluigi.expenseregister.infra.database.entity.Tag
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class TagDTO(

    @JsonProperty("id")
    val id: UUID?,

    @field:NotBlank
    @field:Size(min = 3, max = 255)
    @JsonProperty("description")
    val description: String,

){
    constructor(tag: Tag): this(
        tag.id,
        tag.description
    )

    override fun equals(other: Any?): Boolean =
        other is TagDTO && other.id == this.id && other.description == this.description

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + description.hashCode()
        return result
    }
}