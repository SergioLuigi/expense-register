package br.com.sergioluigi.expenseregister.infra.controller

import br.com.sergioluigi.expenseregister.model.dto.ExpenseDTO
import br.com.sergioluigi.expenseregister.model.dto.ExpenseSaveUpdateDTO
import br.com.sergioluigi.expenseregister.model.dto.SearchExpense
import br.com.sergioluigi.expenseregister.modules.expense.ExpenseService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/expenses")
class ExpenseController(
    private val expenseService: ExpenseService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Creates a new expense.")
    fun create(@RequestBody @Valid expenseSaveUpdateDTO: ExpenseSaveUpdateDTO): Mono<ExpenseDTO> =
        expenseService.create(expenseSaveUpdateDTO)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Updates an expense.")
    fun update(@PathVariable id: UUID,
               @RequestBody @Valid expenseSaveUpdateDTO: ExpenseSaveUpdateDTO
    ): Mono<ExpenseDTO> =
        expenseService.update(id, expenseSaveUpdateDTO)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an expense.")
    fun deleteById(@PathVariable id: UUID): Mono<Void> =
        expenseService.deleteById(id)

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return an expenses page.")
    fun findAll(@RequestParam @Valid search: SearchExpense?,
                @PageableDefault(sort = ["createdDate"]) pageable: Pageable): Mono<Page<ExpenseDTO>> =
        expenseService.findAll(search, pageable)

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return an expense by id.")
    fun findById(@PathVariable id: UUID): Mono<ExpenseDTO> =
        expenseService.findById(id)
}