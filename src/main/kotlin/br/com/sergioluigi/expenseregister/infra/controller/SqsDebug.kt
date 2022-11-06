package br.com.sergioluigi.expenseregister.infra.controller

import br.com.sergioluigi.expenseregister.infra.sqs.sender.SqsMessageNewBillSender
import br.com.sergioluigi.expenseregister.model.dto.BillDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sqs")
class SqsDebug(
    private val newBillSender: SqsMessageNewBillSender
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody bill: BillDTO) =
        newBillSender.send(bill)
}