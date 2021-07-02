package edu.utn.udee.Udee.service.backoffice;

import edu.utn.udee.Udee.TestUtils.AddressTestUtils;
import edu.utn.udee.Udee.TestUtils.RateTestUtils;
import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Rate;
import edu.utn.udee.Udee.exceptions.AddressNotExistsException;
import edu.utn.udee.Udee.exceptions.RateExistsException;
import edu.utn.udee.Udee.exceptions.RateNotExistsException;
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

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Conf.class)
public class RateServiceTest {

    private RateService rateService;

    private RateRepository rateRepository;
    private AddressService addressService;

    @Before
    public void setUp(){
        this.rateRepository = mock(RateRepository.class);
        this.addressService = mock(AddressService.class);

        this.rateService = new RateService(rateRepository, addressService);
    }

    @Test
    public void testAddRateOk() throws RateExistsException  {
        when(rateRepository.existsByDescription(any())).thenReturn(false);
        when(rateRepository.save(RateTestUtils.getRateWithoutId())).thenReturn(RateTestUtils.getRateWithId());
        try {
            Rate rate = rateService.addRate(RateTestUtils.getRateWithoutId());
            assertEquals(RateTestUtils.getRateWithId(), rate);
        } catch (RateExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = RateExistsException.class)
    public void testAddRateException() throws RateExistsException {
        when(rateRepository.existsByDescription(any())).thenReturn(true);
        Rate rate = rateService.addRate(RateTestUtils.getRateWithoutId());
    }

    @Test
    public void testGetAll(){
        PageRequest pageable = PageRequest.of(1, 10);
        Page<Rate> mockedPage = mock(Page.class);
        PowerMockito.when(mockedPage.getContent()).thenReturn(RateTestUtils.getRateList());
        when(rateRepository.findAll(pageable)).thenReturn(mockedPage);

        Page<Rate> rate = rateService.getAll(pageable);

        verify(rateRepository, times(1)).findAll(pageable);
        assertEquals(2, rate.getContent().size());
    }

    @Test
    public void testGetByIdOk() throws RateNotExistsException {
        when(rateRepository.findById(any())).thenReturn(Optional.of(RateTestUtils.getRateWithId()));
        try {
            Rate rate = rateService.getById(1);
            assertEquals(RateTestUtils.getRateWithId(), rate);
        } catch (RateNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = RateNotExistsException.class)
    public void testGetByIdException() throws RateNotExistsException {
        Rate rate = rateService.getById(any());
    }

    @Test
    public void testDeleteByIdOk() throws RateNotExistsException {
        when(rateRepository.existsById(any())).thenReturn(true);
        doNothing().when(rateRepository).deleteById(any());

        try {
            rateService.deleteById(1);
            verify(rateRepository, times(1)).deleteById(1);
        } catch (RateNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = RateNotExistsException.class)
    public void testDeleteByIdException() throws RateNotExistsException {
        when(rateRepository.existsById(any())).thenReturn(false);
        rateService.deleteById(1);
    }

    @Test
    public void testEditRateOk() throws  RateNotExistsException {
        when(rateRepository.findById(any())).thenReturn(Optional.ofNullable(RateTestUtils.getRateWithId()));
        when(rateRepository.save(any())).thenReturn(RateTestUtils.getRateWithId());

        try {
            Rate rate = rateService.editRate(RateTestUtils.getRateWithoutId(), 1);
            assertEquals(RateTestUtils.getRateWithId(), rate);
        } catch (RateNotExistsException e) {
            Assert.fail("This test shouldn't throw an exception");
        }
    }

    @Test(expected = RateNotExistsException.class)
    public void testEditRateException() throws  RateNotExistsException {
        Rate rate = rateService.editRate(RateTestUtils.getRateWithoutId(), 1);
    }

    /*@Test
    public void testAddAddressToRateOk() throws RateNotExistsException, AddressNotExistsException {
        when(rateRepository.existsById(any())).thenReturn(true);
        when(rateRepository.findById(any())).thenReturn(Optional.ofNullable(RateTestUtils.getRateWithId()));
        when(addressService.findAddressById(any())).thenReturn(AddressTestUtils.getAddressAdded());
        when(rateRepository.save(any())).thenReturn(RateTestUtils.getRateWithId());

//        try {
            rateService.addAddressToRate(1,1);
            verify(rateRepository, times(1)).save(RateTestUtils.getRateWithId());
        *//*} catch (Exception e) {
            Assert.fail("This test shouldn't throw an exception");
        }*//*
    }

    @Test(expected = RateNotExistsException.class)
    public void testAddAddressToRateRateNotExistsException() throws RateNotExistsException, AddressNotExistsException {
        when(rateRepository.existsById(any())).thenReturn(false);
        rateService.addAddressToRate(1,1);
    }

    @Test(expected = AddressNotExistsException.class)
    public void testAddAddressToRateAddressNotExistsException() throws RateNotExistsException, AddressNotExistsException {
        when(rateRepository.existsById(any())).thenReturn(true);
        when(rateRepository.findById(any())).thenReturn(Optional.ofNullable(RateTestUtils.getRateWithId()));
        when(addressService.findAddressById(any())).thenThrow(new AddressNotExistsException());
        rateService.addAddressToRate(1,1);
    }*/


}
