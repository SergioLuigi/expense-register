package br.com.sergioluigi.expenseregister.util

import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql
import java.lang.annotation.*

@Test
@Inherited
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Sql(scripts = ["/test-container-scripts/expense_integration_test.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = ["/test-container-scripts/expense_integration_test_clear.sql"], executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
annotation class SQLExpenseTagIntegrationTest()
