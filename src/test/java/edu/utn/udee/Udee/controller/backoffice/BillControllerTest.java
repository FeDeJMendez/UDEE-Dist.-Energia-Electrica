package edu.utn.udee.Udee.controller.backoffice;

import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Bill;
import edu.utn.udee.Udee.dto.BillDto;
import edu.utn.udee.Udee.exceptions.AddressNotExistsException;
import edu.utn.udee.Udee.exceptions.BillNotExistsException;
import edu.utn.udee.Udee.exceptions.ClientNotExistsException;
import edu.utn.udee.Udee.exceptions.MeterNotExistsException;
import edu.utn.udee.Udee.service.backoffice.AddressService;
import edu.utn.udee.Udee.service.backoffice.BillService;
import edu.utn.udee.Udee.service.backoffice.ClientService;
import edu.utn.udee.Udee.service.backoffice.MeterService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static edu.utn.udee.Udee.TestUtils.AddressTestUtils.getAddressAdded;
import static edu.utn.udee.Udee.TestUtils.BillTestUtils.*;
import static edu.utn.udee.Udee.TestUtils.ClientTestUtils.getClientWithId;
import static edu.utn.udee.Udee.TestUtils.MeterTestUtils.getMeterNoMeasurementList;
import static edu.utn.udee.Udee.TestUtils.MeterTestUtils.getMeterWithMeasurementList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Conf.class)
public class BillControllerTest {

    @Mock
    private BillService billService;
    @Mock
    private MeterService meterService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private AddressService addressService;
    @Mock
    private ClientService clientService;

    @InjectMocks
    private BillController billController;

    @Test
    public void testCreateAllBillsOk()
            throws MeterNotExistsException {
        //BEHAVIORS//
        when(meterService.getAll()).thenReturn(getMeterWithMeasurementList());
        when(billService.createAllBills(any())).thenReturn(getBillList());
        try{
            //EXECUTION//
            ResponseEntity<List<BillDto>> responseEntity = billController.createAllBills();
            //ASSERTS//
            assertEquals(getBillDtoList(), responseEntity.getBody());
            assertEquals(2, responseEntity.getBody().size());
            assertEquals((Double) 100.00, responseEntity.getBody().get(0).getTotalAmount());
            assertEquals((Double) 200.00, responseEntity.getBody().get(1).getTotalAmount());
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        } catch (Exception ex) {
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = MeterNotExistsException.class)
    public void testCreateAllBillsMeterNotExistsException()
            throws MeterNotExistsException{
        when(meterService.getAll()).thenReturn(getMeterWithMeasurementList());
        when(billService.createAllBills(any())).thenThrow(new MeterNotExistsException());
        ResponseEntity responseEntity = billController.createAllBills();
    }

    @Test
    public void testCreateAllBillsNoContent()
            throws MeterNotExistsException {
        //BEHAVIORS//
        when(meterService.getAll()).thenReturn(getMeterNoMeasurementList());
        when(billService.createAllBills(any())).thenReturn(emptyList());
        try {
            //EXECUTION//
            ResponseEntity<List<BillDto>> responseEntity = billController.createAllBills();
            //ASSERTS//
            assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
            assertEquals(0, responseEntity.getBody().size());
        }
        catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testGetAllOk() {
        //BEHAVIORS//
        Pageable pageable = PageRequest.of(1, 10);
        Page<Bill> mockedPage = mock(Page.class);
        when(mockedPage.getTotalElements()).thenReturn(100L);
        when(mockedPage.getTotalPages()).thenReturn(10);
        when(mockedPage.getContent()).thenReturn(getBillList());
        when(billService.getAll(pageable)).thenReturn(mockedPage);
        try {
            //EXECUTION//
            ResponseEntity<List<BillDto>> responseEntity = billController.getAll(pageable);
            //ASSERTS//
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(100L, Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
            assertEquals(10, Integer.parseInt(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
            assertEquals(getBillList(), responseEntity.getBody());
        }
        catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testGetAllNoContent() {
        //BEHAVIORS//
        Pageable pageable = PageRequest.of(50, 10);
        Page<Bill> mockedPage = mock(Page.class);
        when(mockedPage.getContent()).thenReturn(emptyList());
        when(billService.getAll(pageable)).thenReturn(mockedPage);
        try {
            //EXECUTION//
            ResponseEntity<List<BillDto>> responseEntity = billController.getAll(pageable);
            //ASSERTS//
            assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
            assertEquals(0, responseEntity.getBody().size());
        }
        catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testGetByIdOk()
            throws BillNotExistsException {
        //BEHAVIORS//
        when(billService.getById(anyInt())).thenReturn(getBill());
        try {
            //EXECUTION//
            ResponseEntity<BillDto> responseEntity = billController.getById(1);
            //ASSERTS//
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(getBillDto(), responseEntity.getBody());
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = BillNotExistsException.class)
    public void testGetByIdNotExistsException()
            throws BillNotExistsException{
        when(billService.getById(any())).thenThrow(new BillNotExistsException());
        ResponseEntity responseEntity = billController.getById(0);
    }

    @Test
    public void testAddressDebtOk()
            throws AddressNotExistsException {
        //BEHAVIORS//
        when(addressService.findAddressById(1)).thenReturn(getAddressAdded());
        when(billService.addressDebt(any())).thenReturn(getBillListByAddressOrClient());
        try{
            //EXECUTION//
            ResponseEntity<List<BillDto>> responseEntity = billController.addressDebt(1);
            //ASSERTS//
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(getBillDtoListByAddressOrClient(), responseEntity.getBody());
            assertEquals(2, responseEntity.getBody().size());
            assertEquals((Double) 100.00, responseEntity.getBody().get(0).getTotalAmount());
            assertEquals((Double) 200.00, responseEntity.getBody().get(1).getTotalAmount());
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = AddressNotExistsException.class)
    public void testAddressDebtNotExistsException()
            throws AddressNotExistsException{
        when(addressService.findAddressById(anyInt())).thenThrow(new AddressNotExistsException());
        when(billService.addressDebt(any())).thenReturn(getBillListByAddressOrClient());
        ResponseEntity responseEntity = billController.addressDebt(0);
    }

    @Test
    public void testAddressDebtNoContent()
            throws AddressNotExistsException {
        //BEHAVIORS//
        when(addressService.findAddressById(1)).thenReturn(getAddressAdded());
        when(billService.addressDebt(any())).thenReturn(emptyList());
        try {
            //EXECUTION//
            ResponseEntity<List<BillDto>> responseEntity = billController.addressDebt(1);
            //ASSERTS//
            assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
            assertEquals(0, responseEntity.getBody().size());
        }
        catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testClientDebtOk()
            throws ClientNotExistsException {
        //BEHAVIORS//
        when(clientService.findClientById(1)).thenReturn(getClientWithId());
        when(billService.clientDebt(any())).thenReturn(getBillListByAddressOrClient());
        try{
            //EXECUTION//
            ResponseEntity<List<BillDto>> responseEntity = billController.clientDebt(1);
            //ASSERTS//
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(getBillDtoListByAddressOrClient(), responseEntity.getBody());
            assertEquals(2, responseEntity.getBody().size());
            assertEquals((Double) 100.00, responseEntity.getBody().get(0).getTotalAmount());
            assertEquals((Double) 200.00, responseEntity.getBody().get(1).getTotalAmount());
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = ClientNotExistsException.class)
    public void testClientDebtNotExistsException()
            throws ClientNotExistsException{
        when(clientService.findClientById(anyInt())).thenThrow(new ClientNotExistsException());
        when(billService.addressDebt(any())).thenReturn(getBillListByAddressOrClient());
        ResponseEntity responseEntity = billController.clientDebt(0);
    }

    @Test
    public void testClientDebtNoContent()
            throws ClientNotExistsException {
        //BEHAVIORS//
        when(clientService.findClientById(1)).thenReturn(getClientWithId());
        when(billService.clientDebt(any())).thenReturn(emptyList());
        try {
            //EXECUTION//
            ResponseEntity<List<BillDto>> responseEntity = billController.clientDebt(1);
            //ASSERTS//
            assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
            assertEquals(0, responseEntity.getBody().size());
        }
        catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testDeleteBillOk()
            throws BillNotExistsException{
        doNothing().when(billService).deleteById(1);
        try {
            ResponseEntity responseEntity = billController.deleteBill(1);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            verify(billService, times(1)).deleteById(any());
        } catch (BillNotExistsException e) {
            Assert.fail("Unexpected Exception!");
        }
    }

    @Test(expected = BillNotExistsException.class)
    public void testDeleteNotExistsException()
            throws BillNotExistsException{
        doThrow(new BillNotExistsException()).when(billService).deleteById(anyInt());
        ResponseEntity responseEntity = billController.deleteBill(0);
    }
}
