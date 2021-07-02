package edu.utn.udee.Udee.service.backoffice;

import edu.utn.udee.Udee.TestUtils.MeterTestUtils;
import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Meter;
import edu.utn.udee.Udee.exceptions.AddressNotExistsException;
import edu.utn.udee.Udee.exceptions.AddressWithMeterException;
import edu.utn.udee.Udee.exceptions.MeterNotExistsException;
import edu.utn.udee.Udee.repository.MeterRepository;
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
import static edu.utn.udee.Udee.TestUtils.MeterTestUtils.*;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Conf.class)
public class MeterServiceTest {

    @Mock
    private MeterRepository meterRepository;
    @Mock
    private MeasurementService measurementService;
    @Mock
    private AddressService addressService;

    @InjectMocks
    private MeterService meterService;

    @Test
    public void testAddMeterOk()
            throws AddressNotExistsException, AddressWithMeterException {
        when(addressService.findAddressById(anyInt())).thenReturn(getAddressAdded());
        when(meterRepository.findByAddress(any())).thenReturn(null);
        when(meterRepository.save(any())).thenReturn(getMeterAdded());
        try{
            Meter newMeter = meterService.addMeter(getMeterReceived());
            assertEquals(getMeterAdded(), newMeter);
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = AddressNotExistsException.class)
    public void testAddMeterAddressNotExistsException()
            throws AddressNotExistsException, AddressWithMeterException {
        doThrow(new AddressNotExistsException()).when(addressService).findAddressById(any());
        meterService.addMeter(getMeterReceived());
    }

    @Test(expected = AddressWithMeterException.class)
    public void testAddMeterAddressWithMeterException()
            throws AddressNotExistsException, AddressWithMeterException {
        when(addressService.findAddressById(anyInt())).thenReturn(getAddressAdded());
        when(meterRepository.findByAddress(any())).thenReturn(getMeterAdded());
        meterService.addMeter(getMeterReceived());
    }

    @Test
    public void testAddMeterWhitoutAddress()
            throws AddressNotExistsException, AddressWithMeterException{
        when(meterRepository.save(any())).thenReturn(getMeterWithoutAddress());
        Meter meter = meterService.addMeter(getMeterWithoutAddress());
        assertEquals(null, meter.getAddress());
    }

    @Test
    public void testGetAllReturnPageableOk (){
        PageRequest pageable = PageRequest.of(1, 10);
        Page<Meter> mockedPage = mock(Page.class);
        PowerMockito.when(mockedPage.getContent()).thenReturn(getMeterNoMeasurementList());
        when(meterRepository.findAll(pageable)).thenReturn(mockedPage);
        try {
            Page<Meter> meters = meterService.getAll(pageable);
            verify(meterRepository, times(1)).findAll(pageable);
            assertEquals(2, meters.getContent().size());
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testGetAllReturnPageableEmpty (){
        PageRequest pageable = PageRequest.of(1, 10);
        Page<Meter> mockedPage = mock(Page.class);
        PowerMockito.when(mockedPage.getContent()).thenReturn(emptyList());
        when(meterRepository.findAll(pageable)).thenReturn(mockedPage);
        try {
            Page<Meter> meters = meterService.getAll(pageable);
            verify(meterRepository, times(1)).findAll(pageable);
            assertEquals(0, meters.getContent().size());
            assertEquals(emptyList(), meters.getContent());
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testGetAllReturnListOk (){
        List<Meter> meterList = getMeterNoMeasurementList();
        Iterable<Meter> iterableMeterList = meterList;
        when(meterRepository.findAll()).thenReturn(iterableMeterList);
        try {
            List<Meter> meters = meterService.getAll();
            verify(meterRepository, times(1)).findAll();
            assertEquals(2, meters.size());
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testGetAllReturnListEmpty (){
        Iterable<Meter> iterableMeterList = emptyList();
        when(meterRepository.findAll()).thenReturn(iterableMeterList);
        try {
            List<Meter> meters = meterService.getAll();
            verify(meterRepository, times(1)).findAll();
            assertEquals(0, meters.size());
            assertEquals(emptyList(), meters);
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testGetBySerialNumberOk()
            throws MeterNotExistsException {
        when(meterRepository.findById(any())).thenReturn(Optional.of(getMeterAdded()));
        try{
            Meter meter = meterService.getBySerialNumber(1);
            assertEquals(getMeterAdded(), meter);
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = MeterNotExistsException.class)
    public void testGetBySerialNumberNotExistsException()
            throws MeterNotExistsException {
        Meter meter = meterService.getBySerialNumber(0);
    }

    @Test
    public void testGetByAddressOk()
            throws MeterNotExistsException{
        when(meterRepository.findByAddress(any())).thenReturn(getMeterAdded());
        try {
            Meter meter = meterService.getByAddress(getAddressAdded());
            assertEquals(getMeterAdded(), meter);
            assertEquals((Integer) 1, meter.getSerialNumber());
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = MeterNotExistsException.class)
    public void testGetByAddressMeterNotExistsException()
            throws MeterNotExistsException {
        when(meterRepository.findByAddress(any())).thenReturn(null);
        meterService.getByAddress(getAddressAdded());
    }

    @Test
    public void testDeleteBySerialNumberOk()
            throws MeterNotExistsException{
        when(meterRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(meterRepository).deleteById(any());
        try {
            meterService.deleteBySerialNumber(1);
            verify(meterRepository, times(1)).deleteById(1);
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = MeterNotExistsException.class)
    public void testDeleteBySerialNumberNotExistsException()
            throws MeterNotExistsException {
        when(meterRepository.existsById(anyInt())).thenReturn(false);
        meterService.deleteBySerialNumber(1);
    }

    @Test
    public void testEditMeterOk()
            throws MeterNotExistsException{
        when(meterRepository.findById(any())).thenReturn(Optional.ofNullable(getMeterAdded()));
        when(meterRepository.save(any())).thenReturn(getMeterAdded());
        try {
            Meter editedMeter = meterService.editMeter(getMeterAdded(), 1);
            assertEquals(getMeterAdded(), editedMeter);
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = MeterNotExistsException.class)
    public void testEditMeterNotExistsException()
            throws MeterNotExistsException {
        meterService.editMeter(getMeterReceived(),0);
    }

    @Test
    public void testAddAddressToMeterOk()
            throws MeterNotExistsException, AddressNotExistsException{
        when(meterRepository.existsById(anyInt())).thenReturn(true);
        when(meterRepository.findById(any())).thenReturn(Optional.of(getMeterAdded()));
        when(addressService.findAddressById(anyInt())).thenReturn(getAddressAdded());
        when(meterRepository.save(any())).thenReturn(getMeterAdded());
        try{
            meterService.addAddressToMeter(1,1);
            verify(addressService, times(1)).findAddressById(anyInt());
            verify(meterRepository, times(1)).save(any());
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = MeterNotExistsException.class)
    public void testAddAddressToMeterNotExistsException()
            throws MeterNotExistsException, AddressNotExistsException {
        when(meterRepository.existsById(anyInt())).thenReturn(false);
        meterService.addAddressToMeter(0,1);
    }

    @Test(expected = AddressNotExistsException.class)
    public void testAddAddressToMeterAddressNotExistsException()
            throws MeterNotExistsException, AddressNotExistsException {
        when(meterRepository.existsById(anyInt())).thenReturn(true);
        when(meterRepository.findById(any())).thenReturn(Optional.of(getMeterAdded()));
//        doThrow(new AddressNotExistsException()).when(addressService).findAddressById(anyInt());
        when(addressService.findAddressById(anyInt())).thenThrow(new AddressNotExistsException());
        meterService.addAddressToMeter(1,0);
    }

    @Test
    public void testSetBilledMeasurementAllUnbilledOk()
            throws MeterNotExistsException{
        when(meterRepository.existsById(anyInt())).thenReturn(true);
        when(meterRepository.save(any())).thenReturn(getMeterAdded());
        try{
            meterService.setBilledMeasurement(getMeterWithMeasurement());
            verify(meterRepository, times(1)).existsById(anyInt());
            verify(meterRepository, times(1)).save(any());
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testSetBilledMeasurementPartialUnbilledOk()
            throws MeterNotExistsException{
        when(meterRepository.existsById(anyInt())).thenReturn(true);
        when(meterRepository.save(any())).thenReturn(getMeterAdded());
        try{
            meterService.setBilledMeasurement(getMeterWithMeasurementsBilled());
            verify(meterRepository, times(1)).existsById(anyInt());
            verify(meterRepository, times(1)).save(any());
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = MeterNotExistsException.class)
    public void testSetBilledMeasurementMeterNotExistsException()
            throws MeterNotExistsException {
        when(meterRepository.existsById(anyInt())).thenReturn(false);
        meterService.setBilledMeasurement(getMeterAdded());
    }
}
