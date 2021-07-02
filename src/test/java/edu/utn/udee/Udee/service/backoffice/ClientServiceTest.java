package edu.utn.udee.Udee.service.backoffice;

import edu.utn.udee.Udee.TestUtils.ClientTestUtils;
import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Client;
import edu.utn.udee.Udee.exceptions.ClientExistsException;
import edu.utn.udee.Udee.exceptions.ClientNotExistsException;
import edu.utn.udee.Udee.repository.ClientRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Conf.class)
public class ClientServiceTest {

    private ClientService clientService;

    private ClientRepository clientRepository;

    @Before
    public void setUp(){
        this.clientRepository = mock(ClientRepository.class);
        this.clientService = new ClientService(clientRepository);
    }

    @Test
    public void testAddClientOk() throws ClientExistsException {
        when(clientRepository.existsByDni(any())).thenReturn(false);
        when(clientRepository.save(ClientTestUtils.getClientWithoutId())).thenReturn(ClientTestUtils.getClientWithId());

        try {
            Client client = clientService.addClient(ClientTestUtils.getClientWithoutId());
            assertEquals(ClientTestUtils.getClientWithId(), client);
        } catch (ClientExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = ClientExistsException.class)
    public void testAddClientDniAlreadyExists() throws ClientExistsException {
        when(clientRepository.existsByDni(any())).thenReturn(true);
        Client client = clientService.addClient(ClientTestUtils.getClientWithoutId());
    }

    @Test
    public void testEditClientOk () throws ClientNotExistsException {
        when(clientRepository.existsByDni(any())).thenReturn(true);
        when(clientRepository.getByDni(any())).thenReturn(ClientTestUtils.getClientWithId());
        when(clientRepository.save(ClientTestUtils.getClientWithoutId())).thenReturn(ClientTestUtils.getClientWithId());

        try {
            Client client = clientService.editClient(ClientTestUtils.getClientWithoutId(), 1234);
            assertEquals(ClientTestUtils.getClientWithId(), client);
        } catch (ClientNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = ClientNotExistsException.class)
    public void testEditException() throws ClientNotExistsException {
        when(clientRepository.existsByDni(any())).thenReturn(false);
        Client client = clientService.editClient(ClientTestUtils.getClientWithoutId(), 1234);
    }

    @Test
    public void testAllClients(){
        PageRequest pageable = PageRequest.of(1, 10);
        Page<Client> mockedPage = mock(Page.class);
        when(mockedPage.getContent()).thenReturn(ClientTestUtils.getClientsList());
        when(clientRepository.findAll(pageable)).thenReturn(mockedPage);

        Page<Client> client = clientService.allClients(pageable);

        verify(clientRepository, times(1)).findAll(pageable);
        assertEquals(2, client.getContent().size());
    }

    @Test
    public void testGetTenMoreConsumersByDateTimeRange() {
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now().plusMonths(1);
        when(clientRepository.findTenMoreConsumersByDateTimeRange(begin,end)).thenReturn(ClientTestUtils.getClientsList());

        List<Client> clientList = clientService.getTenMoreConsumersByDateRange(begin,end);

        assertEquals(ClientTestUtils.getClientsList(), clientList);
    }

    @Test
    public void testFindClientByIdOk() throws ClientNotExistsException {
        when(clientRepository.findById(any())).thenReturn(Optional.of(ClientTestUtils.getClientWithId()));

        try {
            Client client = clientService.findClientById(1);
            assertEquals(ClientTestUtils.getClientWithId(), client);
        } catch (ClientNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = ClientNotExistsException.class)
    public void testFindClientByIdException() throws ClientNotExistsException {
        Client client = clientService.findClientById(any());
    }

    @Test
    public void testDeleteClientByIdOk() throws ClientNotExistsException {
        when(clientRepository.findById(any())).thenReturn(Optional.of(ClientTestUtils.getClientWithId()));
        doNothing().when(clientRepository).delete(ClientTestUtils.getClientWithId());

        try {
            clientService.deleteClientById(1);
            verify(clientRepository,times(1)).delete(ClientTestUtils.getClientWithId());
        } catch (ClientNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = ClientNotExistsException.class)
    public void testDeleteClientByIdException() throws ClientNotExistsException {
        clientService.deleteClientById(1);
    }

}
