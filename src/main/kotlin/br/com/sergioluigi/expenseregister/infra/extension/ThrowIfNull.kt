package br.com.sergioluigi.expenseregister.infra.extension

import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterError
import reactor.core.publisher.Mono

fun <T: Any> throwIfNull(error: ExpenseRegisterError, func: () -> T?) : Mono<T> =
    Mono.justOrEmpty(func())
        .switchIfEmptyThrow(error)