package edu.utn.udee.Udee.controller.backoffice;

import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Address;
import edu.utn.udee.Udee.dto.AddressDto;
import edu.utn.udee.Udee.exceptions.*;
import edu.utn.udee.Udee.service.backoffice.AddressService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static edu.utn.udee.Udee.TestUtils.AddressTestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Conf.class)
public class AddressControllerTest {

    @Mock
    private AddressService addressService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AddressController addressController;

    @Test
    public void testAddAddressOk()
            throws AddressExistsException, ClientNotExistsException, RateNotExistsException, ClientIsRequiredException, RateIsRequiredException {
        //BEHAVIORS//
        // when(modelMapper.map(addressDtoReceived, Address.class)).thenReturn(addressReceived);
        PowerMockito.mockStatic(Conf.class);
        when(addressService.addAddress(getAddressReceived())).thenReturn(getAddressAdded());
        try {
            //EXECUTION//
            ResponseEntity responseEntity = addressController.addAddress(getAddressDtoReceived());
            //ASSERTS//
            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        } catch (Exception ex) {
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = AddressExistsException.class)
    public void testAddAddressExistsException()
            throws AddressExistsException, ClientNotExistsException, RateNotExistsException, ClientIsRequiredException, RateIsRequiredException  {
        when(addressService.addAddress(any())).thenThrow(new AddressExistsException());
        ResponseEntity responseEntity = addressController.addAddress(getAddressDtoReceived());
    }

    @Test(expected = ClientNotExistsException.class)
    public void testAddClientNotExistsException()
            throws AddressExistsException, ClientNotExistsException, RateNotExistsException, ClientIsRequiredException, RateIsRequiredException  {
        when(addressService.addAddress(any())).thenThrow(new ClientNotExistsException());
        ResponseEntity responseEntity = addressController.addAddress(getAddressDtoReceived());
    }

    @Test(expected = RateNotExistsException.class)
    public void testAddRateNotExistsException()
            throws AddressExistsException, ClientNotExistsException, RateNotExistsException, ClientIsRequiredException, RateIsRequiredException  {
        when(addressService.addAddress(any())).thenThrow(new RateNotExistsException());
        ResponseEntity responseEntity = addressController.addAddress(getAddressDtoReceived());
    }

    @Test(expected = ClientIsRequiredException.class)
    public void testAddWithoutClient()
            throws AddressExistsException, ClientNotExistsException, RateNotExistsException, ClientIsRequiredException, RateIsRequiredException  {
        ResponseEntity responseEntity = addressController.addAddress(getAddressDtoReceivedWithoutClient());
    }

    @Test(expected = RateIsRequiredException.class)
    public void testAddWithoutRate()
            throws AddressExistsException, ClientNotExistsException, RateNotExistsException, ClientIsRequiredException, RateIsRequiredException  {
        ResponseEntity responseEntity = addressController.addAddress(getAddressDtoReceivedWithoutRate());
    }

    @Test
    public void testEditAddressOk ()
            throws AddressNotExistsException {
        when(addressService.editAddress(getAddressReceived(), 1)).thenReturn(getAddressAdded());
        try{
            ResponseEntity responseEntity = addressController.editAddress(getAddressDtoReceived(),1);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }
        catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = AddressNotExistsException.class)
    public void testEditAddressNotExistsException()
            throws AddressNotExistsException{
        when(addressService.editAddress(any(),anyInt())).thenThrow(new AddressNotExistsException());
        ResponseEntity responseEntity = addressController.editAddress(getAddressDtoReceived(),0);
    }

    @Test
    public void testAllAddressOk(){
        //BEHAVIORS//
        Pageable pageable = PageRequest.of(1, 10);
        Page<Address> mockedPage = mock(Page.class);
        when(mockedPage.getTotalElements()).thenReturn(100L);
        when(mockedPage.getTotalPages()).thenReturn(10);
        when(mockedPage.getContent()).thenReturn(getAddressList());
        when(addressService.allAddress(pageable)).thenReturn(mockedPage);
        try {
            //EXECUTION//
            ResponseEntity<List<AddressDto>> responseEntity = addressController.allAddress(pageable);
            //ASSERTS//
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(100L, Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
            assertEquals(10, Integer.parseInt(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
            assertEquals(getAddressList(), responseEntity.getBody());
        }
        catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testAllAddressNoContent() {
        //BEHAVIORS//
        Pageable pageable = PageRequest.of(50, 10);
        Page<Address> mockedPage = mock(Page.class);
        when(mockedPage.getContent()).thenReturn(Collections.emptyList());
        when(addressService.allAddress(pageable)).thenReturn(mockedPage);
        try {
            //EXECUTION//
            ResponseEntity<List<AddressDto>> responseEntity = addressController.allAddress(pageable);
            //ASSERTS//
            Assertions.assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
            Assertions.assertEquals(0, responseEntity.getBody().size());
        }
        catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testDeleteAddressByIdOk()
            throws AddressNotExistsException{
        doNothing().when(addressService).deleteAddressById(1);
        try {
            ResponseEntity responseEntity = addressController.deleteAddressById(1);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            verify(addressService, times(1)).deleteAddressById(any());
        } catch (AddressNotExistsException e) {
            Assert.fail("Unexpected Exception!");
        }
    }

    @Test(expected = AddressNotExistsException.class)
    public void testDeleteAddressNotExistsException()
            throws AddressNotExistsException{
        doThrow(new AddressNotExistsException()).when(addressService).deleteAddressById(anyInt());
        ResponseEntity responseEntity = addressController.deleteAddressById(0);
    }
}
