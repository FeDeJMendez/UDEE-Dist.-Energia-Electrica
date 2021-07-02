package edu.utn.udee.Udee.service.client;

import edu.utn.udee.Udee.TestUtils.BillTestUtils;
import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Bill;
import edu.utn.udee.Udee.repository.ClientBillRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Conf.class)
public class ClientBillServiceTest {

    private ClientBillService clientBillService;

    private ClientBillRepository clientBillRepository;

    @Before
    public void setUp(){
        this.clientBillRepository = mock(ClientBillRepository.class);

        this.clientBillService = new ClientBillService(clientBillRepository);
    }

    @Test
    public void testGetBillsBetweenDates(){
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        when(clientBillRepository.getAllBetweenDates(begin, end, 1)).thenReturn(BillTestUtils.getBillList());
        List<Bill> billList = clientBillService.getBillsBetweenDates(begin, end, 1);
        assertEquals(BillTestUtils.getBillList(), billList);
    }

    @Test
    public void testGetUnpaidBills() {
        when(clientBillRepository.findUnpaidBillsByDni(any())).thenReturn(BillTestUtils.getBillList());
        List<Bill> billList = clientBillService.getUnpaidBills(1234);
        assertEquals(BillTestUtils.getBillList(), billList);
    }
}
