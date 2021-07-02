package edu.utn.udee.Udee.controller.backoffice;

import edu.utn.udee.Udee.TestUtils.ClientTestUtils;
import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Client;
import edu.utn.udee.Udee.dto.ClientDto;
import edu.utn.udee.Udee.exceptions.ClientExistsException;
import edu.utn.udee.Udee.exceptions.ClientNotExistsException;
import edu.utn.udee.Udee.service.UserService;
import edu.utn.udee.Udee.service.backoffice.ClientService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Conf.class)
public class ClientControllerTest {

    private ClientController clientController;

    private ClientService clientService;
    private UserService userService;
    private ModelMapper modelMapper;

    @Before
    public void setUp() {
        this.clientService = mock(ClientService.class);
        this.userService = mock(UserService.class);
        this.modelMapper = mock(ModelMapper.class);
        this.clientController = new ClientController(clientService, userService, modelMapper);
    }

    @Test
    public void testAddClientOk() throws ClientExistsException {

        ClientDto clientDto = ClientTestUtils.getClientDto();
        Client clientWithoutId = ClientTestUtils.getClientWithoutId();
        Client clientWithId =ClientTestUtils.getClientWithId();

        PowerMockito.mockStatic(Conf.class);
        when(clientService.addClient(clientWithoutId)).thenReturn(clientWithId);
        doNothing().when(userService).addUser(clientWithId);

        try {
            ResponseEntity responseEntity = clientController.addClient(clientDto);
            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
            verify(userService, times(1)).addUser(any());
        } catch (ClientExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = ClientExistsException.class)
    public void testAddClientException() throws ClientExistsException {
        when(clientService.addClient(any())).thenThrow(new ClientExistsException());
        ResponseEntity responseEntity = clientController.addClient(ClientTestUtils.getClientDto());
    }

    @Test
    public void testEditClientOk() throws ClientNotExistsException {
        when(clientService.editClient(ClientTestUtils.getClientWithoutId(), 1234)).thenReturn(ClientTestUtils.getClientWithoutId());

        try {
            ResponseEntity responseEntity = clientController.editClient(ClientTestUtils.getClientDto(), 1234);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        } catch (ClientNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = ClientNotExistsException.class)
    public void testEditClientException() throws ClientNotExistsException {
        when(clientService.editClient(any(),any())).thenThrow(new ClientNotExistsException());
        ResponseEntity responseEntity = clientController.editClient(ClientTestUtils.getClientDto(), 9999);
    }

    @Test
    public void testAllClientsHttpStatus200(){
        PageRequest pageable = PageRequest.of(1, 10);
        Page<Client> mockedPage = mock(Page.class);
        when(mockedPage.getTotalElements()).thenReturn(100L);
        when(mockedPage.getTotalPages()).thenReturn(10);
        when(mockedPage.getContent()).thenReturn(ClientTestUtils.getClientsList());
        when(clientService.allClients(pageable)).thenReturn(mockedPage);

        ResponseEntity<List<ClientDto>> responseEntity = clientController.allClients(pageable);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testAllClientsHttpStatusNonContent(){
        PageRequest pageable = PageRequest.of(50, 10);
        Page<Client> mockedPage = mock(Page.class);
        when(mockedPage.getTotalElements()).thenReturn(100L);
        when(mockedPage.getTotalPages()).thenReturn(10);
        when(mockedPage.getContent()).thenReturn(ClientTestUtils.getEmptyClientList());
        when(clientService.allClients(pageable)).thenReturn(mockedPage);

        ResponseEntity<List<ClientDto>> responseEntity = clientController.allClients(pageable);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(0, responseEntity.getBody().size());
    }

    @Test
    public void testGetTenMoreConsumersByDateRangeOk(){
        LocalDateTime beginDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.now().plusMonths(1);

        when(clientService.getTenMoreConsumersByDateRange(beginDateTime.toLocalDate(), endDateTime.toLocalDate())).thenReturn(ClientTestUtils.getClientsList());

        ResponseEntity<List<ClientDto>> listResponseEntity = clientController.getTenMoreConsumersByDateTimeRange(beginDateTime.toLocalDate(), endDateTime.toLocalDate());

        assertEquals(HttpStatus.OK, listResponseEntity.getStatusCode());
    }

    @Test
    public void testGetTenMoreConsumersByDateRangeNonContent(){
        LocalDateTime beginDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = LocalDateTime.now().plusMonths(1);

        when(clientService.getTenMoreConsumersByDateRange(beginDateTime.toLocalDate(), endDateTime.toLocalDate())).thenReturn(ClientTestUtils.getEmptyClientList());

        ResponseEntity<List<ClientDto>> listResponseEntity = clientController.getTenMoreConsumersByDateTimeRange(beginDateTime.toLocalDate(), endDateTime.toLocalDate());

        assertEquals(HttpStatus.NO_CONTENT, listResponseEntity.getStatusCode());
    }

    @Test
    public void testDeleteClientByIdOk() throws ClientNotExistsException {
        doNothing().when(clientService).deleteClientById(1);

        try {
            ResponseEntity responseEntity = clientController.deleteClientById(1);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            verify(clientService, times(1)).deleteClientById(any());
        } catch (ClientNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = ClientNotExistsException.class)
    public void testDeleteClientByIdException() throws ClientNotExistsException {
        doThrow(new ClientNotExistsException()).when(clientService).deleteClientById(any());
        ResponseEntity responseEntity = clientController.deleteClientById(any());
    }


}
