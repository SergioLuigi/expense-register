package br.com.sergioluigi.expenseregister.infra.extension

import org.springframework.data.jpa.repository.JpaRepository

fun <T: Any, ID: Any> JpaRepository<T, ID>.findByIdOrNull(id: ID): T? =
    findById(id).orElse(null)