package br.com.sergioluigi.expenseregister.model.util.annotation

import br.com.sergioluigi.expenseregister.model.util.constraintvalidator.ValidatePeriodValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidatePeriodValidator::class])
annotation class ValidatePeriod(
    val message: String = "Initial date must be older than final Date",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)
