package br.com.sergioluigi.expenseregister.extension

import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterError
import br.com.sergioluigi.expenseregister.model.exception.ExpenseRegisterException

fun Throwable.isExceptionErrorEqual(error: ExpenseRegisterError): Boolean =
    this is ExpenseRegisterException && this.error == error