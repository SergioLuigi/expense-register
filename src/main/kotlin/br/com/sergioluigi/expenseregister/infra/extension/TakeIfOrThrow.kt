package br.com.sergioluigi.expenseregister.infra.extension

import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterError
import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterException

inline fun <T> T.takeIfOrThrow(error: ExpenseRegisterError, predicate: (T) -> Boolean): T =
    takeIf{
        predicate(it)
    } ?: throw ExpenseRegisterException(error)