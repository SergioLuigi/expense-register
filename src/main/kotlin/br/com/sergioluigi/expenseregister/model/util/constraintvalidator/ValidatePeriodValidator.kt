package br.com.sergioluigi.expenseregister.model.util.constraintvalidator

import br.com.sergioluigi.expenseregister.model.dto.SearchExpense
import br.com.sergioluigi.expenseregister.model.util.annotation.ValidatePeriod
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class ValidatePeriodValidator: ConstraintValidator<ValidatePeriod, SearchExpense> {

    override fun isValid(value: SearchExpense, context: ConstraintValidatorContext?): Boolean =
        value.checkPeriod()
}