package br.com.sergioluigi.expenseregister.modules.bill

import br.com.sergioluigi.expenseregister.infra.database.entity.Bill
import br.com.sergioluigi.expenseregister.infra.database.repository.BillRepository
import br.com.sergioluigi.expenseregister.model.dto.BillDTO
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BillServiceImpl(
    private val billRepository: BillRepository
): BillService {

    private val logger = KotlinLogging.logger {  }

    @Transactional
    override fun saveBill(dto: BillDTO){
        val bill = Bill(dto)
        billRepository.save(bill)
    }

}