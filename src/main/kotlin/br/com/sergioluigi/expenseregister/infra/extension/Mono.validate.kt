package br.com.sergioluigi.expenseregister.infra.extension

import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterError
import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterException
import reactor.core.publisher.Mono

fun <T: Any> Mono<T>.validate(error: ExpenseRegisterError, predicate: () -> Boolean): Mono<T> =
    if(!predicate()) Mono.error(ExpenseRegisterException(error)) else this