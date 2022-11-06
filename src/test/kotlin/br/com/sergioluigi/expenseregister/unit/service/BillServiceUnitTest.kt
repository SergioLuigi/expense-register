package br.com.sergioluigi.expenseregister.unit.service


import br.com.sergioluigi.expenseregister.infra.database.entity.Bill
import br.com.sergioluigi.expenseregister.infra.database.repository.BillRepository
import br.com.sergioluigi.expenseregister.model.dto.BillDTO
import br.com.sergioluigi.expenseregister.modules.bill.BillServiceImpl
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@ExtendWith(MockKExtension::class)
internal class BillServiceUnitTest {

    @RelaxedMockK
    lateinit var billRepository: BillRepository

    @InjectMockKs
    lateinit var billService: BillServiceImpl

    lateinit var bill: Bill

    lateinit var billDTO: BillDTO

    @BeforeEach
    fun beforeEach(){
        billDTO = BillDTO(UUID.randomUUID(), BigDecimal.valueOf(10.0), LocalDate.now())
        bill = Bill(billDTO)
    }

    @Test
    fun `Given a bill when saveBill then return persisted bill`(){

        every { billRepository.save(any()) } returns bill

        val result = billService.saveBill(billDTO)

        result.shouldBe(bill)
    }

}