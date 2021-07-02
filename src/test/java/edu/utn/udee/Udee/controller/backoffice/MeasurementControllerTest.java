package edu.utn.udee.Udee.controller.backoffice;

import edu.utn.udee.Udee.TestUtils.MeasurementTestUtils;
import edu.utn.udee.Udee.config.Conf;
import edu.utn.udee.Udee.domain.Measurement;
import edu.utn.udee.Udee.dto.MeasurementDto;
import edu.utn.udee.Udee.exceptions.AddressNotExistsException;
import edu.utn.udee.Udee.exceptions.MeasurementNotExistsException;
import edu.utn.udee.Udee.exceptions.MeterIsRequiredException;
import edu.utn.udee.Udee.exceptions.MeterNotExistsException;
import edu.utn.udee.Udee.service.backoffice.AddressService;
import edu.utn.udee.Udee.service.backoffice.MeasurementService;
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

import java.time.LocalDate;
import java.util.List;

import static edu.utn.udee.Udee.TestUtils.AddressTestUtils.getAddressAdded;
import static edu.utn.udee.Udee.TestUtils.MeasurementTestUtils.*;
import static edu.utn.udee.Udee.TestUtils.MeterTestUtils.getMeterAdded;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Conf.class)
public class MeasurementControllerTest {

    @Mock
    private MeasurementService measurementService;
    @Mock
    private MeterService meterService;
    @Mock
    private AddressService addressService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private NullPointerException nullPointerException;

    @InjectMocks
    private MeasurementController measurementController;

    @Test
    public void testAddMeasurementOk()
            throws MeterNotExistsException, MeterIsRequiredException {
        //BEHAVIORS//
        PowerMockito.mockStatic(Conf.class);
        when(measurementService.addMeasurement(any())).thenReturn(getUnbilledMeasurement());
        when(meterService.getBySerialNumber(anyInt())).thenReturn(getMeterAdded());
        try {
            //EXECUTION//
            ResponseEntity responseEntity = measurementController.addMeasurement(getNewMeasurementDto());
            //ASSERTS//
            assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        } catch (Exception ex) {
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = MeterNotExistsException.class)
    public void testAddMeasurementMeterNotExistsException()
            throws MeterNotExistsException, MeterIsRequiredException{
        when(measurementService.addMeasurement(any())).thenReturn(getUnbilledMeasurement());
        when(meterService.getBySerialNumber(anyInt())).thenThrow(new MeterNotExistsException());
        ResponseEntity responseEntity = measurementController.addMeasurement(getNewMeasurementDto());
    }

    @Test(expected = MeterIsRequiredException.class)
    public void testAddMeasurementMeterIsRequiredException()
            throws MeterNotExistsException, MeterIsRequiredException{
        ResponseEntity responseEntity = measurementController.addMeasurement(getUnbilledMeasurementDtoWithoutMeter());
    }

    @Test
    public void testGetAllOk() {
        //BEHAVIORS//
        Pageable pageable = PageRequest.of(1, 10);
        Page<Measurement> mockedPage = mock(Page.class);
        when(mockedPage.getTotalElements()).thenReturn(100L);
        when(mockedPage.getTotalPages()).thenReturn(10);
        when(mockedPage.getContent()).thenReturn(getMeasurementUnbilledList());
        when(measurementService.getAll(pageable)).thenReturn(mockedPage);
        try {
            //EXECUTION//
            ResponseEntity<List<MeasurementDto>> responseEntity = measurementController.getAll(pageable);
            //ASSERTS//
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(100L, Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
            assertEquals(10, Integer.parseInt(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
            assertEquals(getMeasurementUnbilledList(), responseEntity.getBody());
        }
        catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testGetAllNoContent() {
        //BEHAVIORS//
        Pageable pageable = PageRequest.of(50, 10);
        Page<Measurement> mockedPage = mock(Page.class);
        when(mockedPage.getContent()).thenReturn(emptyList());
        when(measurementService.getAll(pageable)).thenReturn(mockedPage);
        try {
            //EXECUTION//
            ResponseEntity<List<MeasurementDto>> responseEntity = measurementController.getAll(pageable);
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
            throws MeasurementNotExistsException {
        //BEHAVIORS//
        when(measurementService.getById(anyInt())).thenReturn(getUnbilledMeasurement());
        try {
            //EXECUTION//
            ResponseEntity<MeasurementDto> responseEntity = measurementController.getById(1);
            //ASSERTS//
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }
        catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = MeasurementNotExistsException.class)
    public void testGetByIdNotExistsException()
            throws MeasurementNotExistsException{
        when(measurementService.getById(anyInt())).thenThrow(new MeasurementNotExistsException());
        ResponseEntity responseEntity = measurementController.getById(0);
    }

    @Test
    public void testGetByAddressAndDateTimeRangeOk()
            throws AddressNotExistsException, MeterNotExistsException{
        //BEHAVIORS//
        LocalDate startDate = LocalDate.of(2021,01,01);
        LocalDate endDate = LocalDate.of(2021,12,01);
        when(addressService.findAddressById(anyInt())).thenReturn(getAddressAdded());
        when(meterService.getByAddress(any())).thenReturn(getMeterAdded());
        when(measurementService.getByMeterAndDateTimeRange(anyInt(), any(), any())).thenReturn(MeasurementTestUtils.getMeasurementUnbilledList());
        try {
            //EXECUTION//
            ResponseEntity<List<MeasurementDto>> responseEntity = measurementController.getByAddressAndDateTimeRange(1,startDate, endDate);
            //ASSERTS//
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        } catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test(expected = AddressNotExistsException.class)
    public void testGetByAddressAndDateTimeRangeAddressNotExistsException()
            throws AddressNotExistsException, MeterNotExistsException{
        LocalDate startDate = LocalDate.of(2021,01,01);
        LocalDate endDate = LocalDate.of(2021,12,01);
        when(addressService.findAddressById(anyInt())).thenThrow(new AddressNotExistsException());
        ResponseEntity responseEntity = measurementController.getByAddressAndDateTimeRange(1,startDate, endDate);
    }

    @Test(expected = MeterNotExistsException.class)
    public void testGetByAddressAndDateTimeRangeMeterNotExistsException()
            throws AddressNotExistsException, MeterNotExistsException{
        LocalDate startDate = LocalDate.of(2021,01,01);
        LocalDate endDate = LocalDate.of(2021,12,01);
        when(addressService.findAddressById(anyInt())).thenReturn(getAddressAdded());
        when(meterService.getByAddress(any())).thenThrow(new MeterNotExistsException());
        ResponseEntity responseEntity = measurementController.getByAddressAndDateTimeRange(1,startDate, endDate);
    }

    @Test
    public void testGetByAddressAndDateTimeRangeNoContent()
            throws AddressNotExistsException, MeterNotExistsException{
        //BEHAVIORS//
        LocalDate startDate = LocalDate.of(2021,01,01);
        LocalDate endDate = LocalDate.of(2021,12,01);
        when(addressService.findAddressById(anyInt())).thenReturn(getAddressAdded());
        when(meterService.getByAddress(any())).thenReturn(getMeterAdded());
        when(measurementService.getByMeterAndDateTimeRange(anyInt(), any(), any())).thenReturn(emptyList());
        try {
            //EXECUTION//
            ResponseEntity<List<MeasurementDto>> responseEntity = measurementController.getByAddressAndDateTimeRange(1,startDate, endDate);
            //ASSERTS//
            assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
            assertEquals(0, responseEntity.getBody().size());
        }
        catch (Exception ex){
            fail("Unexpected Exception!");
        }
    }

    @Test
    public void testDeleteMeasurementOk()
            throws MeasurementNotExistsException{
        doNothing().when(measurementService).deleteById(1);
        try {
            ResponseEntity responseEntity = measurementController.deleteMeasurement(1);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            verify(measurementService, times(1)).deleteById(any());
        } catch (MeasurementNotExistsException e) {
            Assert.fail("Unexpected Exception!");
        }
    }

    @Test(expected = MeasurementNotExistsException.class)
    public void testDeleteMeasurementNotExistsException()
            throws MeasurementNotExistsException{
        doThrow(new MeasurementNotExistsException()).when(measurementService).deleteById(anyInt());
        ResponseEntity responseEntity = measurementController.deleteMeasurement(0);
    }
}
