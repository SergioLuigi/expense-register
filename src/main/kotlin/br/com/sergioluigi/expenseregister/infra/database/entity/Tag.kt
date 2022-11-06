package br.com.sergioluigi.expenseregister.infra.database.entity

import br.com.sergioluigi.expenseregister.model.dto.TagDTO
import org.hibernate.envers.Audited
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Audited
@Table(name = "tag")
@EntityListeners(AuditingEntityListener::class)
data class Tag(

    @Id
    @GeneratedValue
    var id: UUID?,

    @Column(name = "description", nullable = false, unique = true)
    val description: String,

    @ManyToMany(mappedBy = "tags")
    val expenses: Set<Expense> = emptySet()

){

    constructor(tagDTO: TagDTO): this(
        tagDTO.id,
        tagDTO.description
    )

    constructor(tagDiscription: String): this(
        null,
        tagDiscription
    )

    @CreatedDate
    @Column(name = "created_date")
    lateinit var createdDate: LocalDateTime

    @LastModifiedDate
    @Column(name = "updated_date")
    lateinit var updatedDate: LocalDateTime

}
