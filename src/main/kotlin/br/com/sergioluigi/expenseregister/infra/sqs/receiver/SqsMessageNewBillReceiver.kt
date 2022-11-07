package br.com.sergioluigi.expenseregister.infra.sqs.receiver

import br.com.sergioluigi.expenseregister.infra.sqs.MessageReceiver
import br.com.sergioluigi.expenseregister.infra.sqs.Queues.EXPENSE_REGISTER_NEW_BILL
import br.com.sergioluigi.expenseregister.model.dto.BillDTO
import br.com.sergioluigi.expenseregister.modules.bill.BillService
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy
import io.awspring.cloud.messaging.listener.annotation.SqsListener
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class SqsMessageNewBillReceiver(
    private val billService: BillService
): MessageReceiver<BillDTO>() {

    private val logger = KotlinLogging.logger {  }

    @SqsListener(*[EXPENSE_REGISTER_NEW_BILL], deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    override fun execute(obj: BillDTO) {
        try {
            billService.saveBill(obj)
        } catch (ex: Exception) {
            logger.error { ex }
        }
    }
}



