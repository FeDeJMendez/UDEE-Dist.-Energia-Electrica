package edu.utn.udee.Udee.service.backoffice;

import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Bill;
import edu.utn.udee.Udee.exceptions.BillNotExistsException;
import edu.utn.udee.Udee.exceptions.MeterNotExistsException;
import edu.utn.udee.Udee.repository.BillRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static edu.utn.udee.Udee.TestUtils.AddressTestUtils.getAddressAdded;
import static edu.utn.udee.Udee.TestUtils.BillTestUtils.getBill;
import static edu.utn.udee.Udee.TestUtils.BillTestUtils.getBillList;
import static edu.utn.udee.Udee.TestUtils.ClientTestUtils.getClientWithAddresses;
import static edu.utn.udee.Udee.TestUtils.MeasurementTestUtils.getMeasurementUnbilledList;
import static edu.utn.udee.Udee.TestUtils.MeterTestUtils.getMeterWithMeasurementList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Conf.class)
public class BillServiceTest {

    @Mock
    private BillRepository billRepository;
    @Mock
    private MeasurementService measurementService;
    @Mock
    private MeterService meterService;

    @InjectMocks
    private BillService billService;

    @Test
    public void testCreateAllBillsOk () throws MeterNotExistsException {
        when(measurementService.getUnbilledMeasurements(any())).thenReturn(getMeasurementUnbilledList());
        doNothing().when(meterService).setBilledMeasurement(any());
        try{
            List<Bill> newBillList = billService.createAllBills(getMeterWithMeasurementList());
            assertEquals(2, newBillList.size());
            assertEquals((Double)4.0, newBillList.get(0).getTotalAmount());
            assertEquals((Double)4.0, newBillList.get(1).getTotalAmount());
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = MeterNotExistsException.class)
    public void testCreateAllBillsMeterNotExistsException()
            throws MeterNotExistsException{
        when(measurementService.getUnbilledMeasurements(any())).thenReturn(getMeasurementUnbilledList());
        doThrow(new MeterNotExistsException()).when(meterService).setBilledMeasurement(any());
        billService.createAllBills(getMeterWithMeasurementList());
    }

    @Test
    public void testCreateAllBillsEmptyList () throws MeterNotExistsException {
        when(measurementService.getUnbilledMeasurements(any())).thenReturn(emptyList());
        doNothing().when(meterService).setBilledMeasurement(any());
        try{
            List<Bill> newBillList = billService.createAllBills(getMeterWithMeasurementList());
            assertEquals(0, newBillList.size());
            assertEquals(emptyList(), newBillList);
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testGetAllOk (){
        PageRequest pageable = PageRequest.of(1, 10);
        Page<Bill> mockedPage = mock(Page.class);
        PowerMockito.when(mockedPage.getContent()).thenReturn(getBillList());
        when(billRepository.findAll(pageable)).thenReturn(mockedPage);
        try {
            Page<Bill> bills = billService.getAll(pageable);

            verify(billRepository, times(1)).findAll(pageable);
            assertEquals(2, bills.getContent().size());
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testGetAllEmptyList (){
        PageRequest pageable = PageRequest.of(1, 10);
        Page<Bill> mockedPage = mock(Page.class);
        PowerMockito.when(mockedPage.getContent()).thenReturn(emptyList());
        when(billRepository.findAll(pageable)).thenReturn(mockedPage);
        try {
            Page<Bill> bills = billService.getAll(pageable);
            verify(billRepository, times(1)).findAll(pageable);
            assertEquals(0, bills.getContent().size());
            assertEquals(emptyList(), bills.getContent());
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testGetByIdOk()
            throws BillNotExistsException {
        when(billRepository.findById(any())).thenReturn(Optional.of(getBill()));
        try{
            Bill bill = billService.getById(1);
            assertEquals(getBill(), bill);
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = BillNotExistsException.class)
    public void testGetByIdNotExistsException()
            throws BillNotExistsException {
        Bill bill = billService.getById(0);
    }

    @Test
    public void testAddressDebtOk (){
        when(billRepository.findUnpaidByAddress(any())).thenReturn(getBillList());
        try{
            List<Bill> billsUnpaid = billService.addressDebt(getAddressAdded());
            assertEquals(2, billsUnpaid.size());
            assertEquals((Double)100.00, billsUnpaid.get(0).getTotalAmount());
            assertEquals((Double)200.00, billsUnpaid.get(1).getTotalAmount());
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testAddressDebtEmptyList (){
        when(billRepository.findUnpaidByAddress(any())).thenReturn(emptyList());
        try{
            List<Bill> unpaidBills = billService.addressDebt(getAddressAdded());
            verify(billRepository, times(1)).findUnpaidByAddress(any());
            assertEquals(0, unpaidBills.size());
            assertEquals(emptyList(), unpaidBills);
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testClientDebt(){
        when(billRepository.findUnpaidByAddress(any())).thenReturn(getBillList());
        try {
            List<Bill> unpaidBills = billService.clientDebt(getClientWithAddresses());
            assertEquals(4, unpaidBills.size());
            assertEquals((Double)100.00, unpaidBills.get(0).getTotalAmount());
            assertEquals((Double)200.00, unpaidBills.get(1).getTotalAmount());
            assertEquals((Double)100.00, unpaidBills.get(2).getTotalAmount());
            assertEquals((Double)200.00, unpaidBills.get(3).getTotalAmount());
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testClientDebtEmptyList(){
        when(billRepository.findUnpaidByAddress(any())).thenReturn(emptyList());
        try {
            List<Bill> unpaidBills = billService.clientDebt(getClientWithAddresses());
            verify(billRepository, times(2)).findUnpaidByAddress(any());
            assertEquals(0, unpaidBills.size());
            assertEquals(emptyList(), unpaidBills);
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testDeleteByIdOk()
            throws BillNotExistsException{
        when(billRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(billRepository).deleteById(any());
        try {
            billService.deleteById(1);
            verify(billRepository, times(1)).deleteById(1);
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = BillNotExistsException.class)
    public void testDeleteByIdNotExistsException()
            throws BillNotExistsException {
        when(billRepository.existsById(anyInt())).thenReturn(false);
        billService.deleteById(1);
    }
}
