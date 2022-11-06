package br.com.sergioluigi.expenseregister.modules.bill

import br.com.sergioluigi.expenseregister.model.dto.BillDTO

interface BillService {
    fun saveBill(dto: BillDTO)
}