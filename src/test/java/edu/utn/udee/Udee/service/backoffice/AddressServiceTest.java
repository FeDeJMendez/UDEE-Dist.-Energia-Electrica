package edu.utn.udee.Udee.service.backoffice;

import edu.utn.udee.Udee.TestUtils.AddressTestUtils;
import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Address;
import edu.utn.udee.Udee.exceptions.AddressExistsException;
import edu.utn.udee.Udee.exceptions.AddressNotExistsException;
import edu.utn.udee.Udee.exceptions.ClientNotExistsException;
import edu.utn.udee.Udee.exceptions.RateNotExistsException;
import edu.utn.udee.Udee.repository.AddressRepository;
import edu.utn.udee.Udee.repository.ClientRepository;
import edu.utn.udee.Udee.repository.RateRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Conf.class)
public class AddressServiceTest {

    private AddressService addressService;

    private AddressRepository addressRepository;
    private ClientRepository clientRepository;
    private RateRepository rateRepository;

    @Before
    public void setUp(){
        this.addressRepository = mock(AddressRepository.class);
        this.clientRepository = mock(ClientRepository.class);
        this.rateRepository = mock(RateRepository.class);

        this.addressService = new AddressService(addressRepository, clientRepository, rateRepository);
    }

    @Test
    public void testAddAddressOk()  throws AddressExistsException, ClientNotExistsException, RateNotExistsException {
        when(addressRepository.existsByAddress(any())).thenReturn(false);
        when(clientRepository.existsById(any())).thenReturn(true);
        when(rateRepository.existsById(any())).thenReturn(true);
        when(addressRepository.save(AddressTestUtils.getAddressReceived())).thenReturn(AddressTestUtils.getAddressAdded());

        try {
            Address address = addressService.addAddress(AddressTestUtils.getAddressReceived());
            assertEquals(AddressTestUtils.getAddressAdded(), address);
        } catch (Exception e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = AddressExistsException.class)
    public void testAddAddressAddressExistsException() throws AddressExistsException, ClientNotExistsException, RateNotExistsException {
        when(addressRepository.existsByAddress(any())).thenReturn(true);
        Address address = addressService.addAddress(AddressTestUtils.getAddressReceived());
    }

    @Test(expected = ClientNotExistsException.class)
    public void testAddAddressClientNotExistsException() throws AddressExistsException, ClientNotExistsException, RateNotExistsException {
        when(addressRepository.existsByAddress(any())).thenReturn(false);
        when(clientRepository.existsById(any())).thenReturn(false);
        Address address = addressService.addAddress(AddressTestUtils.getAddressReceived());
    }

    @Test(expected = RateNotExistsException.class)
    public void testAddAddressRateNotExistsException() throws AddressExistsException, ClientNotExistsException, RateNotExistsException {
        when(addressRepository.existsByAddress(any())).thenReturn(false);
        when(clientRepository.existsById(any())).thenReturn(true);
        when(rateRepository.existsById(any())).thenReturn(false);
        Address address = addressService.addAddress(AddressTestUtils.getAddressReceived());
    }

    @Test
    public void testAllAddress() {
        PageRequest pageable = PageRequest.of(1, 10);
        Page<Address> mockedPage = mock(Page.class);
        PowerMockito.when(mockedPage.getContent()).thenReturn(AddressTestUtils.getAddressList());
        PowerMockito.when(addressRepository.findAll(pageable)).thenReturn(mockedPage);

        Page<Address> addresses = addressService.allAddress(pageable);

        verify(addressRepository, times(1)).findAll(pageable);
        assertEquals(2, addresses.getContent().size());
    }

    @Test
    public void testDeleteAddressByIdOk() throws AddressNotExistsException {
        when(addressRepository.existsById(any())).thenReturn(true);
        doNothing().when(addressRepository).deleteById(any());

        try{
            addressService.deleteAddressById(any());
            verify(addressRepository, times(1)).deleteById(any());
        } catch (AddressNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = AddressNotExistsException.class)
    public void testDeleteAddressByIdException() throws AddressNotExistsException {
        when(addressRepository.existsById(any())).thenReturn(false);
        addressService.deleteAddressById(any());
    }

    @Test
    public void testEditAddressOk() throws AddressNotExistsException {
        when(addressRepository.findById(any())).thenReturn(Optional.of(AddressTestUtils.getAddressAdded()));

        try {
            Address address = addressService.editAddress(AddressTestUtils.getAddressReceived(), 1);
            assertEquals(AddressTestUtils.getAddressAdded(), address);
        } catch (AddressNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = AddressNotExistsException.class)
    public void testEditAddressException() throws AddressNotExistsException {
        Address address = addressService.editAddress(AddressTestUtils.getAddressReceived(), 1);
    }

    @Test
    public void testFindAddressByIdOk() throws AddressNotExistsException {
        when(addressRepository.findById(any())).thenReturn(Optional.of(AddressTestUtils.getAddressAdded()));
        try {
            Address address = addressService.findAddressById(1);
            assertEquals(AddressTestUtils.getAddressAdded(), address);
        } catch (AddressNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = AddressNotExistsException.class)
    public void testFindAddressByIdException()throws AddressNotExistsException {
        Address address = addressService.findAddressById(1);
    }

    @Test
    public void findAddressByClientIdOk() {
        when(addressRepository.findAddressByClientId(any())).thenReturn(AddressTestUtils.getAddressList());
        List<Address> addressList = addressService.findAddressByClientId(1);
        assertEquals(AddressTestUtils.getAddressList(), addressList);
    }
}
