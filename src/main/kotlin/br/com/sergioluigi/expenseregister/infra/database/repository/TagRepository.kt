package br.com.sergioluigi.expenseregister.infra.database.repository

import br.com.sergioluigi.expenseregister.infra.database.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface TagRepository: JpaRepository<Tag, UUID>, JpaSpecificationExecutor<Tag> {

    fun findAllByDescriptionIn(description: Collection<String>): MutableList<Tag>
    fun existsByDescription(description: String): Boolean
    fun findByDescription(description: String): Tag?
}
