package br.com.sergioluigi.expenseregister.infra.database.specification

import br.com.sergioluigi.expenseregister.infra.database.entity.Expense
import br.com.sergioluigi.expenseregister.infra.database.entity.Tag
import org.springframework.data.jpa.domain.Specification

class TagSpecification {
    fun searchByFirstThreeLetters(name: String): Specification<Tag> =
        Specification { root, query, cb ->
            cb.like(root.get(Expense::description.name),"%$name%")
        }
}