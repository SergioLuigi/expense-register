package br.com.sergioluigi.expenseregister.infra.sqs.sender

import br.com.sergioluigi.expenseregister.infra.sqs.Queues.EXPENSE_REGISTER_NEW_BILL
import br.com.sergioluigi.expenseregister.infra.sqs.SqsMessageSender
import br.com.sergioluigi.expenseregister.model.dto.BillDTO
import io.awspring.cloud.messaging.core.QueueMessagingTemplate
import org.springframework.stereotype.Service
import javax.validation.Valid

@Service
class SqsMessageNewBillSender(
    messagingTemplate: QueueMessagingTemplate,
): SqsMessageSender<BillDTO>(messagingTemplate) {

    fun send(@Valid billDTO: BillDTO){

        send(EXPENSE_REGISTER_NEW_BILL, billDTO)
    }
}

