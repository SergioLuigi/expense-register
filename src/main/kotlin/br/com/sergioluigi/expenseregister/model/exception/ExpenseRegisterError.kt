package br.com.sergioluigi.expenseregister.model.exception

import org.springframework.http.HttpStatus

enum class ExpenseRegisterError(val httpStatus: HttpStatus, val reason: String) {
    MINIMUM_THREE_LETTERS_NEEDED_FOR_SEARCH(HttpStatus.BAD_REQUEST,"minimum.three.letters.needed.for.search"),
    EXPENSE_NOT_FOUND(HttpStatus.NOT_FOUND, "expense.not.found"),
    TAG_NOT_FOUND(HttpStatus.NOT_FOUND, "tag.not.found"),
    TAG_ALREADY_EXISTS(HttpStatus.CONFLICT, "tag.already.exists");
}