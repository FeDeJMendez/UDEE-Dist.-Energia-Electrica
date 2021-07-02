package edu.utn.udee.Udee.controller.client;

import edu.utn.udee.Udee.TestUtils.BillTestUtils;
import edu.utn.udee.Udee.TestUtils.ClientTestUtils;
import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.enums.Rol;
import edu.utn.udee.Udee.dto.BillDto;
import edu.utn.udee.Udee.dto.UserDto;
import edu.utn.udee.Udee.exceptions.ClientNotExistsException;
import edu.utn.udee.Udee.service.UserService;
import edu.utn.udee.Udee.service.backoffice.AddressService;
import edu.utn.udee.Udee.service.backoffice.ClientService;
import edu.utn.udee.Udee.service.client.ClientBillService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Conf.class)
@PowerMockIgnore({
        "javax.security.*"})
public class ClientBillControllerTest {

    private ClientBillController clientBillController;

    private ClientBillService clientBillService;
    private ClientService clientService;
    private UserService userService;
    private AddressService addressService;
    private ModelMapper modelMapper;
    private Authentication auth;

    @Before
    public void setUp(){
        this.clientBillService = mock(ClientBillService.class);
        this.clientService = mock(ClientService.class);
        this.userService = mock(UserService.class);
        this.addressService = mock(AddressService.class);
        this.modelMapper = mock(ModelMapper.class);
        this.auth = mock(Authentication.class);

        this.clientBillController = new ClientBillController(clientBillService, clientService, userService, addressService, modelMapper);
    }

    @Test
    public void testGetBillsBetweenDatesHttpStatusOk() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusMonths(1);
        when(auth.getPrincipal()).thenReturn(UserDto.builder().id(1).username("Amoruso").rol(Rol.ROLE_CLIENT).client_id(1).build());
        when(clientBillService.getBillsBetweenDates(startDate, endDate, 1)).thenReturn(BillTestUtils.getBillList());

        ResponseEntity<List<BillDto>> responseEntity = clientBillController.getBillsBetweenDates(startDate, endDate, auth);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetBillsBetweenDatesHttpStatusNonContent() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusMonths(1);
        when(auth.getPrincipal()).thenReturn(UserDto.builder().id(1).username("Amoruso").rol(Rol.ROLE_CLIENT).client_id(1).build());
        when(clientBillService.getBillsBetweenDates(startDate, endDate, 1)).thenReturn(BillTestUtils.getEmptyBillList());

        ResponseEntity<List<BillDto>> responseEntity = clientBillController.getBillsBetweenDates(startDate, endDate, auth);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testGetUnpaidBillsHttpStatusOk() throws ClientNotExistsException {
        when(auth.getPrincipal()).thenReturn(UserDto.builder().id(1).username("Amoruso").rol(Rol.ROLE_CLIENT).client_id(1).build());
        when(clientService.findClientById(1)).thenReturn(ClientTestUtils.getClientWithId());
        when(clientBillService.getUnpaidBills(1234)).thenReturn(BillTestUtils.getBillList());

        try {
            ResponseEntity<List<BillDto>> responseEntity = clientBillController.getUnpaidBills(auth);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        } catch (ClientNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test
    public void testGetUnpaidBillsHttpStatusNonContent() throws ClientNotExistsException {
        when(auth.getPrincipal()).thenReturn(UserDto.builder().id(1).username("Amoruso").rol(Rol.ROLE_CLIENT).client_id(1).build());
        when(clientService.findClientById(1)).thenReturn(ClientTestUtils.getClientWithId());
        when(clientBillService.getUnpaidBills(1234)).thenReturn(BillTestUtils.getEmptyBillList());

        try {
            ResponseEntity<List<BillDto>> responseEntity = clientBillController.getUnpaidBills(auth);
            assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        } catch (ClientNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = ClientNotExistsException.class)
    public void testGetUnpaidBillsException() throws ClientNotExistsException {
        when(auth.getPrincipal()).thenReturn(UserDto.builder().id(1).username("Amoruso").rol(Rol.ROLE_CLIENT).client_id(1).build());
        when(clientService.findClientById(1)).thenThrow(new ClientNotExistsException());
        ResponseEntity<List<BillDto>> responseEntity = clientBillController.getUnpaidBills(auth);
    }


}
