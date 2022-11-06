package br.com.sergioluigi.expenseregister.infra.database.entity

import br.com.sergioluigi.expenseregister.model.dto.ExpenseSaveUpdateDTO
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.persistence.CascadeType.*
import javax.persistence.FetchType.EAGER


@Entity
@Audited
@Table(name = "expense")
@EntityListeners(AuditingEntityListener::class)
data class Expense(

    @Id
    @GeneratedValue
    var id: UUID?,

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "responsible_name", nullable = false)
    var responsibleName: String,

    @Column(name = "value", nullable = false)
    var value: BigDecimal,

    @NotAudited
    @ManyToMany(fetch = EAGER, cascade = [PERSIST, MERGE])
    @JoinTable(
        name = "expense_tag",
        joinColumns = [
            JoinColumn(name = "expense_id"),
        ],
        inverseJoinColumns = [
            JoinColumn(name = "tag_id")
        ]
    )
    var tags: MutableList<Tag> = mutableListOf()

) {

    constructor(expenseSaveUpdateDTO: ExpenseSaveUpdateDTO, tags: MutableList<Tag>): this(
        id = null,
        description = expenseSaveUpdateDTO.description,
        responsibleName = expenseSaveUpdateDTO.responsibleName,
        value = expenseSaveUpdateDTO.value,
        tags = tags
    )

    fun update(expenseSaveUpdateDTO: ExpenseSaveUpdateDTO): Expense {
        description = expenseSaveUpdateDTO.description
        responsibleName = expenseSaveUpdateDTO.responsibleName
        value = expenseSaveUpdateDTO.value
        return this
    }

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    lateinit var createdDate: LocalDateTime

    @LastModifiedDate
    @Column(name = "updated_date")
    lateinit var updatedDate: LocalDateTime

}