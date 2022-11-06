package br.com.sergioluigi.expenseregister.infra.extension

import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterError
import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterException
import reactor.core.publisher.Mono

fun <T: Any> Mono<T>.switchIfEmptyThrow(error: ExpenseRegisterError): Mono<T> =
    switchIfEmpty(Mono.error(ExpenseRegisterException(error)))


