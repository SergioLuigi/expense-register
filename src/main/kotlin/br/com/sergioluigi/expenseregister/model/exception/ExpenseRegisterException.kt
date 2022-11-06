package br.com.sergioluigi.expenseregister.model.exception

import org.springframework.web.server.ResponseStatusException

class ExpenseRegisterException(val error: ExpenseRegisterError):
    ResponseStatusException(error.httpStatus, error.reason) {
}