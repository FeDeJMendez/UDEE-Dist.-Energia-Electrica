package edu.utn.udee.Udee.controller.backoffice;

import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Meter;
import edu.utn.udee.Udee.dto.MeterDto;
import edu.utn.udee.Udee.exceptions.AddressNotExistsException;
import edu.utn.udee.Udee.exceptions.AddressWithMeterException;
import edu.utn.udee.Udee.exceptions.MeterNotExistsException;
import edu.utn.udee.Udee.service.backoffice.MeterService;
import org.junit.Assert;
import org.junit.Test;
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

import static edu.utn.udee.Udee.TestUtils.MeterTestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Conf.class)
public class MeterControllerTest {

    @Mock
    private MeterService meterService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MeterController meterController;

    @Test
    public void testAddMeterOk()
            throws AddressNotExistsException, AddressWithMeterException {
        //BEHAVIORS//
        PowerMockito.mockStatic(Conf.class);
        when(meterService.addMeter(getMeterReceived())).thenReturn(getMeterAdded());
        try {
            //EXECUTION//
            ResponseEntity responseEntity = meterController.addMeter(getMeterDtoReceived());
            //ASSERTS//
            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        } catch (Exception ex) {
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = AddressNotExistsException.class)
    public void testAddMeterAddressNotExistsException()
            throws AddressNotExistsException, AddressWithMeterException{
        when(meterService.addMeter(any())).thenThrow(new AddressNotExistsException());
        ResponseEntity responseEntity = meterController.addMeter(getMeterDtoReceived());
    }

    @Test(expected = AddressWithMeterException.class)
    public void testAddMeterAddressWithMeterException()
            throws AddressNotExistsException, AddressWithMeterException{
        when(meterService.addMeter(any())).thenThrow(new AddressWithMeterException());
        ResponseEntity responseEntity = meterController.addMeter(getMeterDtoReceived());
    }

    @Test
    public void testGetAllOk() {
        //BEHAVIORS//
        Pageable pageable = PageRequest.of(1, 10);
        Page<Meter> mockedPage = mock(Page.class);
        when(mockedPage.getTotalElements()).thenReturn(100L);
        when(mockedPage.getTotalPages()).thenReturn(10);
        when(mockedPage.getContent()).thenReturn(getMeterNoMeasurementList());
        when(meterService.getAll(pageable)).thenReturn(mockedPage);
        try {
            //EXECUTION//
            ResponseEntity<List<MeterDto>> responseEntity = meterController.getAll(pageable);
            //ASSERTS//
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(100L, Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
            assertEquals(10, Integer.parseInt(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
            assertEquals(getMeterNoMeasurementList(), responseEntity.getBody());
        }
        catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testGetAllNoContent() {
        //BEHAVIORS//
        Pageable pageable = PageRequest.of(50, 10);
        Page<Meter> mockedPage = mock(Page.class);
        when(mockedPage.getContent()).thenReturn(Collections.emptyList());
        when(meterService.getAll(pageable)).thenReturn(mockedPage);
        try {
            //EXECUTION//
            ResponseEntity<List<MeterDto>> responseEntity = meterController.getAll(pageable);
            //ASSERTS//
            assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
            assertEquals(0, responseEntity.getBody().size());
        }
        catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testGetBySerialNumberOk()
            throws MeterNotExistsException {
        //BEHAVIORS//
        when(meterService.getBySerialNumber(anyInt())).thenReturn(getMeterAdded());
        try {
            //EXECUTION//
            ResponseEntity<MeterDto> responseEntity = meterController.getBySerialNumber(1);
            //ASSERTS//
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(getMeterDtoAdded(), responseEntity.getBody());
        }
        catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = MeterNotExistsException.class)
    public void testGetBySerialNumberNotExistsException()
            throws MeterNotExistsException{
        when(meterService.getBySerialNumber(any())).thenThrow(new MeterNotExistsException());
        ResponseEntity responseEntity = meterController.getBySerialNumber(0);
    }

    @Test
    public void testDeleteMeterOk()
            throws MeterNotExistsException{
        doNothing().when(meterService).deleteBySerialNumber(1);
        try {
            ResponseEntity responseEntity = meterController.deleteMeter(1);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            verify(meterService, times(1)).deleteBySerialNumber(any());
        } catch (MeterNotExistsException e) {
            Assert.fail("Unexpected Exception!");
        }
    }

    @Test(expected = MeterNotExistsException.class)
    public void testDeleteMeterNotExistsException()
            throws MeterNotExistsException{
        doThrow(new MeterNotExistsException()).when(meterService).deleteBySerialNumber(anyInt());
        ResponseEntity responseEntity = meterController.deleteMeter(0);
    }

    @Test
    public void testEditMeterOk ()
            throws MeterNotExistsException {
        when(meterService.editMeter(getMeterReceived(), 1)).thenReturn(getMeterAdded());
        try{
            ResponseEntity responseEntity = meterController.editMeter(getMeterDtoReceived(),1);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }
        catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = MeterNotExistsException.class)
    public void testEditMeterNotExistsException()
            throws MeterNotExistsException{
        doThrow(new MeterNotExistsException()).when(meterService).editMeter(any(), anyInt());
        ResponseEntity responseEntity = meterController.editMeter(getMeterDtoReceived(), 0);
    }

    @Test
    public void testAddAddressToMeterOk()
            throws MeterNotExistsException, AddressNotExistsException{
        doNothing().when(meterService).addAddressToMeter(anyInt(),anyInt());
        try {
            ResponseEntity responseEntity = meterController.addAddressToMeter(1, 1);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }
        catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }
}
